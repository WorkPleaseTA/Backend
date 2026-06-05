package com.example.workmanager.substitute.application.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubstituteCandidateResponse {

    private List<CandidateItem> candidates;

    public static SubstituteCandidateResponse from(List<CandidateItem> items) {
        return SubstituteCandidateResponse.builder()
                .candidates(items)
                .build();
    }

    @Getter
    @Builder
    public static class CandidateItem {
        private Long storeMemberId;
        private String name;
    }
}