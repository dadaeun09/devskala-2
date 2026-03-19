package com.devskala.todo.dto;

import com.devskala.todo.domain.Todo;
import java.time.LocalDateTime;

public class TodoResponse {

    private Long id;
    private String title;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoResponse from(Todo todo) {
        TodoResponse response = new TodoResponse();
        response.id = todo.getId();
        response.title = todo.getTitle();
        response.completed = todo.isCompleted();
        response.createdAt = todo.getCreatedAt();
        response.updatedAt = todo.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
