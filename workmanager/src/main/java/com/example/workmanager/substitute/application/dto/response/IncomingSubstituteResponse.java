package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteCandidateStatus;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import com.example.workmanager.substitute.domain.entity.SubstituteRequestStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IncomingSubstituteResponse {

    private Long candidateId;
    private Long requestId;
    private String requesterName;
    private LocalDate requestDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private SubstituteCandidateStatus myStatus;
    private SubstituteRequestStatus requestStatus;

    public static IncomingSubstituteResponse from(SubstituteCandidate candidate) {
        SubstituteRequest req = candidate.getSubstituteRequest();
        return IncomingSubstituteResponse.builder()
                .candidateId(candidate.getId())
                .requestId(req.getId())
                .requesterName(req.getStoreMember().getUser().getName())
                .requestDate(req.getRequestDate())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .reason(req.getReason())
                .myStatus(candidate.getStatus())
                .requestStatus(req.getStatus())
                .build();
    }
}
