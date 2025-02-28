package com.doneflow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordDto {

  @NotBlank(message = "현재 비밀번호는 필수 입력값입니다.")
  private String oldPassword;

  @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
  @Size(min = 6, message = "새 비밀번호는 최소 6자 이상이어야 합니다.")
  private String newPassword;
}
