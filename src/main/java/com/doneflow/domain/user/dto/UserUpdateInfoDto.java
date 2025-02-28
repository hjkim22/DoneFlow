package com.doneflow.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInfoDto {

  @NotBlank(message = "닉네임은 필수 입력값입니다.")
  @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다.")
  private String nickname;
}
