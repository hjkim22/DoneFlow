package com.doneflow.domain.auth.dto;

import com.doneflow.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterResponseDto {

  private Long id;
  private String email;
  private String nickname;

  public static UserRegisterResponseDto from(User user) {
    return new UserRegisterResponseDto(user.getId(), user.getEmail(), user.getNickname());
  }
}
