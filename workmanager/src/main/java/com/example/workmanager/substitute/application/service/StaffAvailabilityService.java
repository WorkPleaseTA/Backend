package com.example.workmanager.substitute.application.service;

import com.example.workmanager.ai.application.service.AiService;
import com.example.workmanager.ai.application.service.AiService.CandidateInfo;
import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.schedule.domain.repository.FixedScheduleRepository;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.example.workmanager.substitute.application.dto.request.StaffAvailabilityCreateRequest;
import com.example.workmanager.substitute.application.dto.response.AvailabilityGridResponse;
import com.example.workmanager.substitute.application.dto.response.AvailabilityGridResponse.AvailabilitySlot;
import com.example.workmanager.substitute.application.dto.response.AvailabilityGridResponse.FixedSlot;
import com.example.workmanager.substitute.application.dto.response.StaffAvailabilityResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteCandidateResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteCandidateResponse.CandidateItem;
import com.example.workmanager.substitute.domain.entity.StaffAvailability;
import com.example.workmanager.substitute.domain.exception.SubstituteErrorCode;
import com.example.workmanager.substitute.domain.repository.StaffAvailabilityRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffAvailabilityService {

    private final StaffAvailabilityRepository availabilityRepository;
    private final FixedScheduleRepository fixedScheduleRepository;
    private final StoreMemberRepository storeMemberRepository;
    private final AiService aiService;

    public StaffAvailabilityResponse createAvailability(Long userId, StaffAvailabilityCreateRequest request) {
        StoreMember storeMember = getActiveMember(userId, request.getStoreId());

        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new BaseException(SubstituteErrorCode.INVALID_TIME_RANGE);
        }

        if (fixedScheduleRepository.existsByStoreMemberIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                storeMember.getId(), request.getDayOfWeek(), request.getEndTime(), request.getStartTime())) {
            throw new BaseException(SubstituteErrorCode.OVERLAPS_WITH_FIXED_SCHEDULE);
        }

        if (availabilityRepository.existsByStoreMemberIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                storeMember.getId(), request.getDayOfWeek(), request.getEndTime(), request.getStartTime())) {
            throw new BaseException(SubstituteErrorCode.OVERLAPS_WITH_EXISTING_AVAILABILITY);
        }

        StaffAvailability availability = availabilityRepository.save(StaffAvailability.builder()
                .storeMember(storeMember)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build());

        return StaffAvailabilityResponse.from(availability);
    }

    public void deleteAvailability(Long userId, Long availabilityId) {
        StaffAvailability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.AVAILABILITY_NOT_FOUND));

        if (!availability.getStoreMember().getUser().getId().equals(userId)) {
            throw new BaseException(SubstituteErrorCode.NOT_YOUR_AVAILABILITY);
        }

        availabilityRepository.delete(availability);
    }

    @Transactional(readOnly = true)
    public AvailabilityGridResponse getAvailabilityGrid(Long userId, Long storeId) {
        StoreMember storeMember = getActiveMember(userId, storeId);

        List<FixedSlot> fixedSlots = fixedScheduleRepository.findAllByStoreMemberId(storeMember.getId())
                .stream()
                .map(FixedSlot::from)
                .toList();

        List<AvailabilitySlot> availabilitySlots = availabilityRepository.findAllByStoreMemberId(storeMember.getId())
                .stream()
                .map(AvailabilitySlot::from)
                .toList();

        return AvailabilityGridResponse.builder()
                .fixedSchedules(fixedSlots)
                .availabilities(availabilitySlots)
                .build();
    }

    @Transactional(readOnly = true)
    public SubstituteCandidateResponse findCandidates(Long userId, Long storeId,
                                                       LocalDate date, LocalTime startTime, LocalTime endTime) {
        StoreMember requester = getActiveMember(userId, storeId);

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        List<StaffAvailability> availabilities = availabilityRepository
                .findAllByStoreMemberStoreIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                        storeId, dayOfWeek, endTime, startTime);

        Map<Long, List<StaffAvailability>> byMember = availabilities.stream()
                .filter(a -> !a.getStoreMember().getId().equals(requester.getId()))
                .collect(Collectors.groupingBy(
                        a -> a.getStoreMember().getId(),
                        LinkedHashMap::new,
                        Collectors.toList()));

        if (byMember.isEmpty()) {
            return SubstituteCandidateResponse.from(List.of());
        }

        List<CandidateInfo> candidateInfos = byMember.values().stream()
                .map(slots -> {
                    StoreMember member = slots.get(0).getStoreMember();
                    String slotsStr = slots.stream()
                            .map(s -> s.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN)
                                    + " " + s.getStartTime() + "-" + s.getEndTime())
                            .collect(Collectors.joining(", "));
                    return new CandidateInfo(member.getId(), member.getUser().getName(), slotsStr);
                })
                .toList();

        String requestDesc = String.format("%s (%s) %s-%s",
                date, dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREAN), startTime, endTime);

        List<Long> rankedIds = aiService.rankCandidates(requestDesc, candidateInfos);

        Map<Long, String> nameMap = candidateInfos.stream()
                .collect(Collectors.toMap(CandidateInfo::storeMemberId, CandidateInfo::name));

        List<CandidateItem> items;
        if (rankedIds.isEmpty()) {
            items = candidateInfos.stream()
                    .map(c -> CandidateItem.builder()
                            .storeMemberId(c.storeMemberId())
                            .name(c.name())
                            .build())
                    .toList();
        } else {
            items = rankedIds.stream()
                    .filter(nameMap::containsKey)
                    .map(id -> CandidateItem.builder()
                            .storeMemberId(id)
                            .name(nameMap.get(id))
                            .build())
                    .toList();
        }

        return SubstituteCandidateResponse.from(items);
    }

    private StoreMember getActiveMember(Long userId, Long storeId) {
        return storeMemberRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.NOT_MEMBER_OF_STORE));
    }
}