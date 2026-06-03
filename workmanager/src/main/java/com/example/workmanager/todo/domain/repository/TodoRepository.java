package com.example.workmanager.todo.domain.repository;

import com.example.workmanager.todo.domain.entity.Todo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByStoreMemberStoreIdOrderByCreatedAtDesc(Long storeId);
}