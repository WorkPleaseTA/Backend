package com.example.workmanager.substitute.application.service;

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
import com.example.workmanager.substitute.domain.entity.StaffAvailability;
import com.example.workmanager.substitute.domain.exception.SubstituteErrorCode;
import com.example.workmanager.substitute.domain.repository.StaffAvailabilityRepository;
import java.util.List;
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

    private StoreMember getActiveMember(Long userId, Long storeId) {
        return storeMemberRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.NOT_MEMBER_OF_STORE));
    }
}