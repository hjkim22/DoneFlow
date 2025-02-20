package com.doneflow.domain.todo.service;

import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.todo.dto.TodoRequestDto;
import com.doneflow.domain.todo.dto.TodoResponseDto;
import com.doneflow.domain.todo.entity.Todo;
import com.doneflow.domain.todo.repository.TodoRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

    return TodoResponseDto.from(todo);
  }

  // 할 일 목록 조회(완료 여부)
  public List<TodoResponseDto> getTodosByStatus(Boolean completed) {
    if (completed == null) {
      return todoRepository.findAll().stream()
          .map(TodoResponseDto::from)
          .collect(Collectors.toList());
    }
    return todoRepository.findAllByCompleted(completed).stream()
        .map(TodoResponseDto::from)
        .collect(Collectors.toList());
  }

  // 카테고리별 할 일 목록 조회
  public List<TodoResponseDto> getAllTodosByCategory(String category) {
    return todoRepository.findAllByCategory(category).stream()
        .map(TodoResponseDto::from)
        .collect(Collectors.toList());
  }

  // 할 일 수정
  @Transactional
  public TodoResponseDto updateTodo(Long id, TodoRequestDto requestDto) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

    todo.setTitle(requestDto.getTitle());
    todo.setContent(requestDto.getContent());
    todo.setCompleted(requestDto.isCompleted());
    todo.setDueDate(requestDto.getDueDate());
    todo.setCategory(requestDto.getCategory());

    return TodoResponseDto.from(todo);
  }

  // 할 일 완료 여부 수정
  @Transactional
  public TodoResponseDto updateCompletedStatus(Long id, Boolean completed) {
    if (completed == null) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }

    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

    todo.setCompleted(completed);

    return TodoResponseDto.from(todo);
  }

  // 할 일 삭제
  public void deleteTodo(Long id) {
    Todo todo = todoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

    todoRepository.delete(todo);
  }
}
