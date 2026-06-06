package com.example.workmanager.ai.application.service;

import com.example.workmanager.ai.domain.exception.AiErrorCode;
import com.example.workmanager.global.common.exception.BaseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public List<String> extractTodos(List<String> formattedMessages) {
        if (formattedMessages.isEmpty()) {
            return List.of();
        }

        String prompt = """
                아래는 업장 채팅방의 최근 대화입니다.
                이 대화에서 해야 할 일(TODO)로 볼 수 있는 항목들을 추출해주세요.
                결과는 반드시 JSON 배열 형식으로만 응답하세요. 예시: ["항목1", "항목2"]
                할 일이 없으면 빈 배열 []을 반환하세요. JSON 외 다른 텍스트는 포함하지 마세요.

                대화 내용:
                %s
                """.formatted(String.join("\n", formattedMessages));

        try {
            String response = chatClient.prompt(prompt).call().content();
            return parseTodoList(response);
        } catch (Exception e) {
            throw new BaseException(AiErrorCode.AI_REQUEST_FAILED);
        }
    }

    /**
     * 후보 목록을 받아 추천 순서로 정렬된 storeMemberId 리스트를 반환.
     * AI 실패 시 원래 순서 그대로 fallback.
     */
    public List<Long> rankCandidates(String requestDescription, List<CandidateInfo> candidates) {
        if (candidates.isEmpty()) {
            return List.of();
        }

        StringBuilder candidateList = new StringBuilder();
        for (int i = 0; i < candidates.size(); i++) {
            CandidateInfo c = candidates.get(i);
            candidateList.append(String.format("%d. storeMemberId=%d, 이름: %s, 가능 시간: %s%n",
                    i + 1, c.storeMemberId(), c.name(), c.availableSlots()));
        }

        String prompt = """
                대타 요청: %s

                아래 직원들이 해당 시간대에 대타 가능한 상태입니다.
                시간대 적합도 기준으로 추천 순서대로 storeMemberId만 JSON 배열로 응답하세요.
                예시: [3, 1, 2]
                JSON 외 다른 텍스트는 포함하지 마세요.

                후보:
                %s
                """.formatted(requestDescription, candidateList);

        try {
            String response = chatClient.prompt(prompt).call().content();
            return parseIdList(response);
        } catch (Exception e) {
            return candidates.stream().map(CandidateInfo::storeMemberId).toList();
        }
    }

    private List<String> parseTodoList(String response) {
        try {
            String cleaned = response.replaceAll("(?s)```json\\s*|```\\s*", "").trim();
            return objectMapper.readValue(cleaned, new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    private List<Long> parseIdList(String response) {
        try {
            String cleaned = response.replaceAll("(?s)```json\\s*|```\\s*", "").trim();
            return objectMapper.readValue(cleaned, new TypeReference<>() {});
        } catch (Exception e) {
            return List.of();
        }
    }

    public record CandidateInfo(Long storeMemberId, String name, String availableSlots) {}
}