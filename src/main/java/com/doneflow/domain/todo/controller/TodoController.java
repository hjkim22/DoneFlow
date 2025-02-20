package com.doneflow.domain.todo.controller;

import com.doneflow.domain.todo.dto.TodoRequestDto;
import com.doneflow.domain.todo.dto.TodoResponseDto;
import com.doneflow.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

  private final TodoService todoService;

  // 할 일 생성
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TodoResponseDto createTodo(@Valid @RequestBody TodoRequestDto requestDto) {
    return todoService.createTodo(requestDto);
  }

  // 할 일 조회
  @GetMapping("/{id}")
  public TodoResponseDto getTodo(@PathVariable("id") Long id) {
    return todoService.getTodoById(id);
  }

  // 할 일 목록 조회
  @GetMapping
  public List<TodoResponseDto> getAllTodos() {
    return todoService.getAllTodos();
  }

  // 할 일 수정
  @PutMapping("/{id}")
  public TodoResponseDto updateTodo(
      @PathVariable("id") Long id, @Valid @RequestBody TodoRequestDto requestDto) {
    return todoService.updateTodo(id, requestDto);
  }

  // 할 일 삭제
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTodo(@PathVariable("id") Long id) {
    todoService.deleteTodo(id);
  }
}
