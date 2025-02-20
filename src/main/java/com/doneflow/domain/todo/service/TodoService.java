package com.doneflow.domain.todo.service;

import com.doneflow.domain.todo.dto.TodoRequestDto;
import com.doneflow.domain.todo.dto.TodoResponseDto;
import com.doneflow.domain.todo.entity.Todo;
import com.doneflow.domain.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;

  // 할 일 생성
  public TodoResponseDto createTodo(TodoRequestDto requestDto) {
    Todo todo = Todo.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .completed(requestDto.isCompleted())
        .dueDate(requestDto.getDueDate())
        .category(requestDto.getCategory())
        .build();

    return TodoResponseDto.from(todoRepository.save(todo));
  }

  // 할 일 조회
  public TodoResponseDto getTodoById(Long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당 Todo를 찾을 수 없습니다. id=" + id));

    return TodoResponseDto.from(todo);
  }

  // 할 일 목록 조회
  public List<TodoResponseDto> getAllTodos() {
    return todoRepository.findAll().stream()
        .map(TodoResponseDto::from)
        .collect(Collectors.toList());
  }

  // 할 일 수정
  public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당 Todo를 찾을 수 없습니다. id=" + id));

    todo = Todo.builder()
        .id(todo.getId())  // 기존 ID 유지
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .completed(requestDto.isCompleted())
        .dueDate(requestDto.getDueDate())
        .category(requestDto.getCategory())
        .build();

    return TodoResponseDto.from(todoRepository.save(todo));
  }

  // 할 일 삭제
  public void deleteTodo(Long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("해당 Todo를 찾을 수 없습니다. id=" + id));

    todoRepository.delete(todo);
  }
}
