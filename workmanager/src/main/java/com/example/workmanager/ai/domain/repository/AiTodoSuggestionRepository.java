package com.example.workmanager.ai.domain.repository;

import com.example.workmanager.ai.domain.entity.AiTodoSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiTodoSuggestionRepository extends JpaRepository<AiTodoSuggestion, Long> {
}