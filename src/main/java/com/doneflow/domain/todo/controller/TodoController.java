package com.doneflow.domain.todo.controller;

import com.doneflow.common.annotation.CurrentUserId;
import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.todo.dto.TodoCompletedDto;
import com.doneflow.domain.todo.dto.TodoRequestDto;
import com.doneflow.domain.todo.dto.TodoResponseDto;
import com.doneflow.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  public List<TodoResponseDto> createTodo(
      @CurrentUserId Long userId, @Valid @RequestBody TodoRequestDto requestDto) {
    return todoService.createTodo(userId, requestDto);
  }

  // 할 일 조회
  @GetMapping("/{id}")
  public TodoResponseDto getTodo(
      @CurrentUserId Long userId, @PathVariable("id") Long id) {
    return todoService.getTodoById(id, userId);
  }

  // 할 일 검색
  @GetMapping("/search")
  public Page<TodoResponseDto> searchTodos(
      @RequestParam(name = "keyword", required = false) String keyword,
      @RequestParam(name = "completed", required = false) Boolean completed,
      @RequestParam(name = "categoryId", required = false) Long categoryId,
      @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
      Pageable pageable) {

    Sort sorting = Sort.by(sort);
    return todoService.searchTodos(keyword, completed, categoryId, sorting, pageable);
  }

  // 할 일 수정
  @PutMapping("/{id}")
  public TodoResponseDto updateTodo(
      @CurrentUserId Long userId,
      @PathVariable("id") Long id, @Valid @RequestBody TodoRequestDto requestDto) {
    return todoService.updateTodo(id, userId, requestDto);
  }

  // 할 일 완료 여부 수정
  @PatchMapping("/{id}/completed")
  public TodoResponseDto updateCompletedStatus(
      @CurrentUserId Long userId,
      @PathVariable("id") Long id,
      @Valid @RequestBody TodoCompletedDto requestDto) {
    return todoService.updateCompletedStatus(id, userId, requestDto.getCompleted());
  }

  // 할 일 삭제
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteTodo(@CurrentUserId Long userId, @PathVariable("id") Long id) {
    todoService.deleteTodo(id, userId);
  }

  // ================== 예외 처리 테스트 ==================

  @GetMapping("/test/server-error")
  public void testServerError() {
    throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}
