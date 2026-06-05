package com.example.workmanager.schedule.application.service;

import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleCreateRequest;
import com.example.workmanager.schedule.application.dto.request.FixedScheduleUpdateRequest;
import com.example.workmanager.schedule.application.dto.response.DailyScheduleResponse;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleEditResponse;
import com.example.workmanager.schedule.application.dto.response.FixedScheduleResponse;
import com.example.workmanager.schedule.application.dto.response.TodayScheduleResponse;
import com.example.workmanager.schedule.domain.entity.FixedSchedule;
import com.example.workmanager.schedule.domain.entity.WorkSchedule;
import com.example.workmanager.schedule.domain.exception.ScheduleErrorCode;
import com.example.workmanager.schedule.domain.repository.FixedScheduleRepository;
import com.example.workmanager.schedule.domain.repository.WorkScheduleRepository;
import com.example.workmanager.member.domain.entity.UserRole;
import com.example.workmanager.store.domain.entity.Store;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.exception.StoreErrorCode;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.example.workmanager.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FixedScheduleService {

    private final FixedScheduleRepository fixedScheduleRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final StoreRepository storeRepository;
    private final StoreMemberRepository storeMemberRepository;

    public void createFixedSchedules(Long userId, Long storeId, FixedScheduleCreateRequest request) {
        Store store = getOwnerStore(userId, storeId);
        DayOfWeek dayOfWeek = request.getDayOfWeek();

        for (FixedScheduleCreateRequest.ScheduleEntry entry : request.getSchedules()) {
            validateTimeRange(entry.getStartTime(), entry.getEndTime());

            StoreMember storeMember = storeMemberRepository.findByIdAndStoreId(entry.getStoreMemberId(), store.getId())
                    .orElseThrow(() -> new BaseException(ScheduleErrorCode.STORE_MEMBER_NOT_IN_STORE));

            if (fixedScheduleRepository.existsByStoreMemberIdAndDayOfWeek(storeMember.getId(), dayOfWeek)) {
                throw new BaseException(ScheduleErrorCode.DUPLICATE_FIXED_SCHEDULE);
            }

            fixedScheduleRepository.save(FixedSchedule.builder()
                    .storeMember(storeMember)
                    .dayOfWeek(dayOfWeek)
                    .startTime(entry.getStartTime())
                    .endTime(entry.getEndTime())
                    .build());
        }
    }

    @Transactional(readOnly = true)
    public List<FixedScheduleResponse> getFixedSchedules(Long userId, Long storeId, DayOfWeek dayOfWeek) {
        getOwnerStore(userId, storeId);

        List<FixedSchedule> schedules = (dayOfWeek != null)
                ? fixedScheduleRepository.findAllByStoreMemberStoreIdAndDayOfWeek(storeId, dayOfWeek)
                : fixedScheduleRepository.findAllByStoreMemberStoreId(storeId);

        return schedules.stream()
                .map(FixedScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FixedScheduleEditResponse> getFixedScheduleEditView(Long userId, Long storeId, DayOfWeek dayOfWeek) {
        getOwnerStore(userId, storeId);

        List<StoreMember> allMembers = storeMemberRepository.findAllByStoreIdAndStatus(storeId, StoreMemberStatus.ACTIVE)
                .stream()
                .filter(m -> m.getUser().getRole() != UserRole.OWNER)
                .toList();

        Map<Long, FixedSchedule> scheduleMap = fixedScheduleRepository
                .findAllByStoreMemberStoreIdAndDayOfWeek(storeId, dayOfWeek)
                .stream()
                .collect(Collectors.toMap(s -> s.getStoreMember().getId(), s -> s));

        return allMembers.stream()
                .map(member -> FixedScheduleEditResponse.of(member, scheduleMap.get(member.getId())))
                .toList();
    }

    public void upsertFixedSchedules(Long userId, Long storeId, FixedScheduleUpdateRequest request) {
        Store store = getOwnerStore(userId, storeId);
        DayOfWeek dayOfWeek = request.getDayOfWeek();

        for (FixedScheduleUpdateRequest.ScheduleEntry entry : request.getSchedules()) {
            validateTimeRange(entry.getStartTime(), entry.getEndTime());

            StoreMember storeMember = storeMemberRepository.findByIdAndStoreId(entry.getStoreMemberId(), store.getId())
                    .orElseThrow(() -> new BaseException(ScheduleErrorCode.STORE_MEMBER_NOT_IN_STORE));

            fixedScheduleRepository.findByStoreMemberIdAndDayOfWeek(storeMember.getId(), dayOfWeek)
                    .ifPresentOrElse(
                            schedule -> schedule.update(entry.getStartTime(), entry.getEndTime()),
                            () -> fixedScheduleRepository.save(FixedSchedule.builder()
                                    .storeMember(storeMember)
                                    .dayOfWeek(dayOfWeek)
                                    .startTime(entry.getStartTime())
                                    .endTime(entry.getEndTime())
                                    .build())
                    );
        }
    }

    public void deleteFixedSchedule(Long userId, Long storeId, Long scheduleId) {
        getOwnerStore(userId, storeId);

        FixedSchedule schedule = fixedScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new BaseException(ScheduleErrorCode.SCHEDULE_NOT_FOUND));

        if (!schedule.getStoreMember().getStore().getId().equals(storeId)) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        fixedScheduleRepository.delete(schedule);
    }

    @Transactional(readOnly = true)
    public List<TodayScheduleResponse> getTodaySchedule(Long userId) {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        return storeMemberRepository.findAllByUserIdAndStatus(userId, StoreMemberStatus.ACTIVE)
                .stream()
                .map(member -> {
                    Optional<WorkSchedule> workSchedule =
                            workScheduleRepository.findByStoreMemberIdAndWorkDate(member.getId(), today);
                    if (workSchedule.isPresent()) {
                        return TodayScheduleResponse.fromWorkSchedule(workSchedule.get());
                    }
                    return fixedScheduleRepository
                            .findByStoreMemberIdAndDayOfWeek(member.getId(), dayOfWeek)
                            .map(TodayScheduleResponse::from)
                            .orElse(null);
                })
                .filter(r -> r != null)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyScheduleResponse> getDailySchedule(Long userId, Long storeId, LocalDate date) {
        StoreMember requester = storeMemberRepository
                .findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(StoreErrorCode.ACCESS_DENIED));

        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Map<Long, WorkSchedule> workScheduleMap = workScheduleRepository
                .findAllByStoreMemberStoreIdAndWorkDate(storeId, date)
                .stream()
                .collect(Collectors.toMap(ws -> ws.getStoreMember().getId(), ws -> ws));

        return storeMemberRepository.findAllByStoreIdAndStatus(storeId, StoreMemberStatus.ACTIVE)
                .stream()
                .filter(m -> m.getUser().getRole() != UserRole.OWNER)
                .flatMap(member -> {
                    if (workScheduleMap.containsKey(member.getId())) {
                        WorkSchedule ws = workScheduleMap.get(member.getId());
                        return java.util.stream.Stream.of(DailyScheduleResponse.builder()
                                .storeMemberId(member.getId())
                                .name(member.getUser().getName())
                                .startTime(ws.getStartTime())
                                .endTime(ws.getEndTime())
                                .isSubstitute(true)
                                .build());
                    }
                    return fixedScheduleRepository
                            .findByStoreMemberIdAndDayOfWeek(member.getId(), dayOfWeek)
                            .map(fs -> DailyScheduleResponse.builder()
                                    .storeMemberId(member.getId())
                                    .name(member.getUser().getName())
                                    .startTime(fs.getStartTime())
                                    .endTime(fs.getEndTime())
                                    .isSubstitute(false)
                                    .build())
                            .stream();
                })
                .toList();
    }

    private Store getOwnerStore(Long userId, Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new BaseException(StoreErrorCode.STORE_NOT_FOUND));

        if (!store.getOwner().getId().equals(userId)) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        return store;
    }

    private void validateTimeRange(LocalTime startTime, LocalTime endTime) {
        if (!startTime.isBefore(endTime)) {
            throw new BaseException(ScheduleErrorCode.INVALID_TIME_RANGE);
        }
    }
}