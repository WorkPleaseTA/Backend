package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import com.example.workmanager.substitute.domain.entity.SubstituteRequestStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StoreSubstituteHistoryResponse {

    private Long requestId;
    private LocalDate requestDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String absenteeName;
    private String substituteName;
    private String message;
    private SubstituteRequestStatus status;

    public static StoreSubstituteHistoryResponse of(SubstituteRequest request, SubstituteCandidate acceptedCandidate) {
        return StoreSubstituteHistoryResponse.builder()
                .requestId(request.getId())
                .requestDate(request.getRequestDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .absenteeName(request.getStoreMember().getUser().getName())
                .substituteName(acceptedCandidate != null
                        ? acceptedCandidate.getStoreMember().getUser().getName()
                        : null)
                .message(request.getMessage())
                .status(request.getStatus())
                .build();
    }
}