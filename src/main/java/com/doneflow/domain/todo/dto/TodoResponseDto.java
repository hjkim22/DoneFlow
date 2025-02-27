package com.doneflow.domain.todo.dto;

import com.doneflow.common.enums.RepeatType;
import com.doneflow.domain.todo.entity.Todo;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoResponseDto {

  private Long userId;
  private Long id;
  private String title;
  private String content;
  private boolean completed;
  private LocalDateTime dueDate;
  private String categoryName;
  private RepeatType repeatType;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TodoResponseDto from(Todo todo) {
    boolean isRepeatedInstance = todo.getRepeatType() == RepeatType.NONE;

    return new TodoResponseDto(
        todo.getUser().getId(),
        todo.getId(),
        todo.getTitle(),
        todo.getContent(),
        todo.isCompleted(),
        todo.getDueDate(),
        todo.getCategory() != null && !"미분류".equals(todo.getCategory().getName())
            ? todo.getCategory().getName()
            : null,
        isRepeatedInstance ? null : todo.getRepeatType(),
        todo.getCreatedAt(),
        todo.getUpdatedAt()
    );
  }
}
