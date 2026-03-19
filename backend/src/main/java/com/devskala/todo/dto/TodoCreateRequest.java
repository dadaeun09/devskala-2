package com.devskala.todo.dto;

import jakarta.validation.constraints.NotBlank;

public class TodoCreateRequest {

    @NotBlank(message = "title은 비어 있을 수 없습니다.")
    private String title;

    public String getTitle() {
        return title;
    }
}
