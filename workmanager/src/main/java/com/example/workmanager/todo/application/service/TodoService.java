package com.example.workmanager.todo.application.service;

import com.example.workmanager.global.common.exception.BaseException;
import com.example.workmanager.store.domain.entity.StoreMember;
import com.example.workmanager.store.domain.entity.StoreMemberStatus;
import com.example.workmanager.store.domain.repository.StoreMemberRepository;
import com.example.workmanager.todo.application.dto.request.TodoCreateRequest;
import com.example.workmanager.todo.application.dto.response.TodoResponse;
import com.example.workmanager.todo.domain.entity.Todo;
import com.example.workmanager.todo.domain.exception.TodoErrorCode;
import com.example.workmanager.todo.domain.repository.TodoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final StoreMemberRepository storeMemberRepository;

    public TodoResponse create(Long userId, TodoCreateRequest request) {
        StoreMember storeMember = getActiveMember(userId, request.getStoreId());
        Todo todo = todoRepository.save(Todo.builder()
                .storeMember(storeMember)
                .content(request.getContent())
                .build());
        return TodoResponse.from(todo);
    }

    @Transactional(readOnly = true)
    public List<TodoResponse> getStoreTodos(Long userId, Long storeId) {
        getActiveMember(userId, storeId);
        return todoRepository.findAllByStoreMemberStoreIdOrderByCreatedAtDesc(storeId)
                .stream()
                .map(TodoResponse::from)
                .toList();
    }

    public TodoResponse toggleDone(Long userId, Long todoId) {
        Todo todo = getTodoAndVerifyMembership(userId, todoId);
        todo.toggleDone();
        return TodoResponse.from(todo);
    }

    public void delete(Long userId, Long todoId) {
        Todo todo = getTodoAndVerifyMembership(userId, todoId);
        todoRepository.delete(todo);
    }

    private Todo getTodoAndVerifyMembership(Long userId, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new BaseException(TodoErrorCode.TODO_NOT_FOUND));
        Long storeId = todo.getStoreMember().getStore().getId();
        getActiveMember(userId, storeId);
        return todo;
    }

    private StoreMember getActiveMember(Long userId, Long storeId) {
        return storeMemberRepository.findByUserIdAndStoreIdAndStatus(userId, storeId, StoreMemberStatus.ACTIVE)
                .orElseThrow(() -> new BaseException(TodoErrorCode.NOT_MEMBER_OF_STORE));
    }
}