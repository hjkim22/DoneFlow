package com.doneflow.domain.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoRequestDto {

  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  private String content;

  @Builder.Default
  private boolean completed = false;

  private LocalDateTime dueDate;

  private Long categoryId;
}
