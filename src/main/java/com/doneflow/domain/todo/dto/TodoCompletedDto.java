package com.doneflow.domain.todo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class TodoCompletedDto {

  @NotNull(message = "completed 값은 필수입니다.")
  private Boolean completed;
}
