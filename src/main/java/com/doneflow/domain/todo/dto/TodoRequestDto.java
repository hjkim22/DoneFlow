package com.doneflow.domain.todo.dto;

import com.doneflow.common.enums.RepeatType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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

  @NotNull(message = "유저 ID는 필수입니다.")
  private Long userId;

  @NotBlank(message = "제목은 필수입니다.")
  private String title;

  private String content;

  @Builder.Default
  private boolean completed = false;

  private LocalDateTime dueDate;

  private Long categoryId;

  private RepeatType repeatType;

  @Min(value = 0, message = "반복 횟수는 0 이상이어야 합니다.")
  @Max(value = 12, message = "반복 횟수는 최대 12까지 가능합니다.")
  private int repeatCount = 0;
}
