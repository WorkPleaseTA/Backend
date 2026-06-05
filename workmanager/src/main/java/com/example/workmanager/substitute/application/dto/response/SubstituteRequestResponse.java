package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteCandidateStatus;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubstituteRequestResponse {

    private Long requestId;
    private Long candidateId;
    private LocalDate requestDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String message;
    private String candidateName;
    private SubstituteCandidateStatus candidateStatus;
    private LocalDateTime createdAt;

    public static SubstituteRequestResponse of(SubstituteRequest request, SubstituteCandidate candidate) {
        return SubstituteRequestResponse.builder()
                .requestId(request.getId())
                .candidateId(candidate.getId())
                .requestDate(request.getRequestDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .message(request.getMessage())
                .candidateName(candidate.getStoreMember().getUser().getName())
                .candidateStatus(candidate.getStatus())
                .createdAt(request.getCreatedAt())
                .build();
    }
}