package com.doneflow.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRegisterRequestDto {

  @Email(message = "유효한 이메일 형식이 아닙니다.")
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력값입니다.")
  @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
  private String password;

  @NotBlank(message = "닉네임은 필수 입력값입니다.")
  @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다.")
  private String nickname;
}
