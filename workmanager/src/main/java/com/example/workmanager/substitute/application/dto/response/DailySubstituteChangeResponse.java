package com.example.workmanager.substitute.application.dto.response;

import com.example.workmanager.substitute.domain.entity.SubstituteCandidate;
import com.example.workmanager.substitute.domain.entity.SubstituteRequest;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DailySubstituteChangeResponse {

    private LocalTime startTime;
    private LocalTime endTime;
    private String absenteeName;
    private String substituteName;

    public static DailySubstituteChangeResponse of(SubstituteRequest request, SubstituteCandidate accepted) {
        return DailySubstituteChangeResponse.builder()
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .absenteeName(request.getStoreMember().getUser().getName())
                .substituteName(accepted.getStoreMember().getUser().getName())
                .build();
    }
}