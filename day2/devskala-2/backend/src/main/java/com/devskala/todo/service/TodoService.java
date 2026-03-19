package com.devskala.todo.service;

import com.devskala.todo.domain.Todo;
import com.devskala.todo.dto.TodoCreateRequest;
import com.devskala.todo.dto.TodoResponse;
import com.devskala.todo.exception.TodoNotFoundException;
import com.devskala.todo.repository.TodoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoResponse> findAll() {
        return todoRepository.findAll().stream()
                .map(TodoResponse::from)
                .toList();
    }

    @Transactional
    public TodoResponse create(TodoCreateRequest request) {
        Todo saved = todoRepository.save(new Todo(request.getTitle()));
        return TodoResponse.from(saved);
    }

    @Transactional
    public TodoResponse toggle(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
        todo.toggleCompleted();
        return TodoResponse.from(todo);
    }

    @Transactional
    public void delete(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        todoRepository.deleteById(id);
    }
}
