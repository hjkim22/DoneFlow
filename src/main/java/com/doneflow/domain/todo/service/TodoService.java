package com.doneflow.domain.todo.service;

import com.doneflow.common.enums.RepeatType;
import com.doneflow.common.exception.CustomException;
import com.doneflow.common.exception.ErrorCode;
import com.doneflow.domain.category.entity.Category;
import com.doneflow.domain.category.repository.CategoryRepository;
import com.doneflow.domain.category.service.CategoryService;
import com.doneflow.domain.todo.dto.TodoRequestDto;
import com.doneflow.domain.todo.dto.TodoResponseDto;
import com.doneflow.domain.todo.entity.Todo;
import com.doneflow.domain.todo.repository.TodoRepository;
import com.doneflow.domain.user.entity.User;
import com.doneflow.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

  private final TodoRepository todoRepository;
  private final CategoryService categoryService;
  private final CategoryRepository categoryRepository;
  private final UserRepository userRepository;

  // 할 일 생성
  @Transactional
  public List<TodoResponseDto> createTodo(Long userId, TodoRequestDto requestDto) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Category category = getCategoryOrDefault(requestDto.getCategoryId());

    Todo todo = Todo.builder()
        .user(user)
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .completed(requestDto.isCompleted())
        .dueDate(requestDto.getDueDate())
        .category(category)
        .repeatType(
            requestDto.getRepeatType() != null ? requestDto.getRepeatType() : RepeatType.NONE)
        .build();

    todoRepository.save(todo);
    List<Todo> createdTodos = new ArrayList<>(List.of(todo));

    int repeatCount = requestDto.getRepeatCount();

    // 반복 할 일 생성
    if (todo.getRepeatType() != RepeatType.NONE) {
      if (repeatCount <= 0) {
        throw new CustomException(ErrorCode.INVALID_REPEAT_COUNT);
      }
      createdTodos.addAll(createRepeatedTodos(todo, repeatCount));
    }

    return createdTodos.stream()
        .map(TodoResponseDto::from)
        .toList();
  }

  // 반복 할 일 자동 생성
  private List<Todo> createRepeatedTodos(Todo originalTodo, int repeatCount) {
    if (originalTodo.getDueDate() == null) {
      throw new CustomException(ErrorCode.REPEAT_TODO_REQUIRES_DUE_DATE);
    }

    List<Todo> repeatedTodos = new ArrayList<>();
    LocalDateTime dueDate = originalTodo.getDueDate();

    for (int i = 1; i <= repeatCount; i++) {
      dueDate = calculateNextDueDate(dueDate, originalTodo.getRepeatType());

      Todo repeatedTodo = Todo.builder()
          .title(originalTodo.getTitle())
          .content(originalTodo.getContent())
          .completed(false)
          .dueDate(dueDate)
          .category(originalTodo.getCategory())
          .repeatType(RepeatType.NONE)
          .build();

      repeatedTodos.add(todoRepository.save(repeatedTodo));
    }

    return repeatedTodos;
  }

  // 할 일 조회
  public TodoResponseDto getTodoById(Long id, Long userId) {
    Todo todo = getTodoIfOwner(id, userId);
    return TodoResponseDto.from(todo);
  }

  // 할 일 검색 (QueryDSL)
  @Transactional(readOnly = true)
  public Page<TodoResponseDto> searchTodos(
      String keyword, Boolean completed, Long categoryId, Sort sort, Pageable pageable) {
    return todoRepository.searchTodos(keyword, completed, categoryId, sort, pageable)
        .map(TodoResponseDto::from);
  }

  // 할 일 수정
  @Transactional
  public TodoResponseDto updateTodo(Long id, Long userId, TodoRequestDto requestDto) {
    Todo todo = getTodoIfOwner(id, userId);
    Category category = categoryService.getCategoryById(requestDto.getCategoryId());

    todo.setTitle(requestDto.getTitle());
    todo.setContent(requestDto.getContent());
    todo.setCompleted(requestDto.isCompleted());
    todo.setDueDate(requestDto.getDueDate());
    todo.setCategory(category);

    return TodoResponseDto.from(todo);
  }

  // 할 일 완료 여부 수정
  @Transactional
  public TodoResponseDto updateCompletedStatus(Long id, Long userId, Boolean completed) {
    if (completed == null) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }

    Todo todo = getTodoIfOwner(id, userId);
    todo.setCompleted(completed);

    return TodoResponseDto.from(todo);
  }

  // 할 일 삭제
  public void deleteTodo(Long id, Long userId) {
    Todo todo = getTodoIfOwner(id, userId);
    todoRepository.delete(todo);
  }

  // ===================== 헬퍼 메서드 =====================

  private Todo getTodoIfOwner(Long todoId, Long userId) {
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new CustomException(ErrorCode.TODO_NOT_FOUND));

    if (!todo.getUser().getId().equals(userId)) {
      throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
    }

    return todo;
  }

  private Category getCategoryOrDefault(Long categoryId) {
    return (categoryId != null)
        ? categoryRepository.findById(categoryId)
        .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
        : categoryRepository.findByName("미분류")
            .orElseGet(() -> categoryRepository.save(new Category(null, "미분류")));
  }

  private LocalDateTime calculateNextDueDate(LocalDateTime dueDate, RepeatType repeatType) {
    return switch (repeatType) {
      case DAILY -> dueDate.plusDays(1);
      case WEEKLY -> dueDate.plusWeeks(1);
      case MONTHLY -> dueDate.plusMonths(1);
      default -> throw new CustomException(ErrorCode.UNSUPPORTED_REPEAT_TYPE);
    };
  }
}
