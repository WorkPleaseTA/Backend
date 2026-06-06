package com.example.workmanager.substitute.application.service;

import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.member.domain.entity.UserRole;
import com.example.workmanager.schedule.domain.entity.WorkSchedule;
import com.example.workmanager.schedule.domain.repository.WorkScheduleRepository;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.exception.StoreErrorCode;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.example.workmanager.substitute.application.dto.request.SubstituteRequestCreateRequest;
import com.example.workmanager.substitute.application.dto.response.DailySubstituteChangeResponse;
import com.example.workmanager.substitute.application.dto.response.IncomingSubstituteResponse;
import com.example.workmanager.substitute.application.dto.response.SubstituteRequestResponse;
import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteCandidateStatus;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import com.example.workmanager.substitute.domain.entity.SubstituteRequestStatus;
import com.example.workmanager.substitute.domain.exception.SubstituteErrorCode;
import com.example.workmanager.substitute.domain.repository.SubstituteCandidateRepository;
import com.example.workmanager.substitute.domain.repository.SubstituteRequestRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SubstituteService {

    private final SubstituteRequestRepository requestRepository;
    private final SubstituteCandidateRepository candidateRepository;
    private final StoreMemberRepository storeMemberRepository;
    private final WorkScheduleRepository workScheduleRepository;

    public List<SubstituteRequestResponse> createRequest(Long userId, SubstituteRequestCreateRequest dto) {
        StoreMember requester = getActiveMember(userId, dto.getStoreId());

        if (!dto.getStartTime().isBefore(dto.getEndTime())) {
            throw new BaseException(SubstituteErrorCode.INVALID_TIME_RANGE);
        }

        SubstituteRequest request = requestRepository.save(SubstituteRequest.builder()
                .storeMember(requester)
                .requestDate(dto.getRequestDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .message(dto.getMessage())
                .build());

        List<SubstituteCandidate> candidates = dto.getSelectedStoreMemberIds().stream()
                .map(storeMemberId -> storeMemberRepository.findByIdAndStoreId(storeMemberId, dto.getStoreId())
                        .orElseThrow(() -> new BaseException(SubstituteErrorCode.NOT_MEMBER_OF_STORE)))
                .map(member -> SubstituteCandidate.builder()
                        .substituteRequest(request)
                        .storeMember(member)
                        .build())
                .toList();

        candidateRepository.saveAll(candidates);

        return candidates.stream()
                .map(c -> SubstituteRequestResponse.of(request, c))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SubstituteRequestResponse> getMyRequests(Long userId, Long storeId) {
        StoreMember storeMember = getActiveMember(userId, storeId);

        return requestRepository.findAllByStoreMemberIdOrderByRequestDateDesc(storeMember.getId())
                .stream()
                .flatMap(request -> candidateRepository
                        .findAllBySubstituteRequestId(request.getId())
                        .stream()
                        .map(candidate -> SubstituteRequestResponse.of(request, candidate)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<IncomingSubstituteResponse> getIncomingRequests(Long userId, Long storeId) {
        StoreMember storeMember = getActiveMember(userId, storeId);

        return candidateRepository.findAllByStoreMemberIdOrderByCreatedAtDesc(storeMember.getId())
                .stream()
                .map(IncomingSubstituteResponse::from)
                .toList();
    }

    public void acceptRequest(Long userId, Long candidateId) {
        SubstituteCandidate candidate = getMyCandidate(userId, candidateId);

        if (candidate.getStatus() != SubstituteCandidateStatus.WAITING) {
            throw new BaseException(SubstituteErrorCode.ALREADY_PROCESSED);
        }
        if (candidate.getSubstituteRequest().getStatus().name().equals("ACCEPTED")) {
            throw new BaseException(SubstituteErrorCode.ALREADY_PROCESSED);
        }

        candidate.accept();
        SubstituteRequest substituteRequest = candidate.getSubstituteRequest();
        substituteRequest.accept();

        workScheduleRepository.save(WorkSchedule.builder()
                .storeMember(candidate.getStoreMember())
                .workDate(substituteRequest.getRequestDate())
                .startTime(substituteRequest.getStartTime())
                .endTime(substituteRequest.getEndTime())
                .substituteRequest(substituteRequest)
                .build());

        // 다른 후보 자동 거절
        candidateRepository.findAllBySubstituteRequestIdAndStatus(
                        substituteRequest.getId(), SubstituteCandidateStatus.WAITING)
                .stream()
                .filter(c -> !c.getId().equals(candidateId))
                .forEach(SubstituteCandidate::decline);
    }

    public void declineRequest(Long userId, Long candidateId) {
        SubstituteCandidate candidate = getMyCandidate(userId, candidateId);

        if (candidate.getStatus() != SubstituteCandidateStatus.WAITING) {
            throw new BaseException(SubstituteErrorCode.ALREADY_PROCESSED);
        }

        candidate.decline();
    }

    @Transactional(readOnly = true)
    public List<DailySubstituteChangeResponse> getDailySubstituteChanges(Long userId, Long storeId, LocalDate date) {
        StoreMember member = storeMemberRepository
                .findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.NOT_MEMBER_OF_STORE));

        if (member.getUser().getRole() != UserRole.OWNER) {
            throw new BaseException(StoreErrorCode.ACCESS_DENIED);
        }

        return requestRepository
                .findAllByStoreMemberStoreIdAndRequestDateAndStatus(storeId, date, SubstituteRequestStatus.ACCEPTED)
                .stream()
                .map(request -> candidateRepository
                        .findAllBySubstituteRequestIdAndStatus(request.getId(), SubstituteCandidateStatus.ACCEPTED)
                        .stream()
                        .findFirst()
                        .map(accepted -> DailySubstituteChangeResponse.of(request, accepted))
                        .orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    private SubstituteCandidate getMyCandidate(Long userId, Long candidateId) {
        SubstituteCandidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.CANDIDATE_NOT_FOUND));

        if (!candidate.getStoreMember().getUser().getId().equals(userId)) {
            throw new BaseException(SubstituteErrorCode.NOT_YOUR_CANDIDATE);
        }

        return candidate;
    }

    private StoreMember getActiveMember(Long userId, Long storeId) {
        return storeMemberRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(SubstituteErrorCode.NOT_MEMBER_OF_STORE));
    }
}