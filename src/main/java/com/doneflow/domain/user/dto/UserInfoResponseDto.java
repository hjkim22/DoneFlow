package com.doneflow.domain.user.dto;

import com.doneflow.common.enums.Role;
import com.doneflow.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

  private Long id;
  private String email;
  private String nickname;
  private Role role;

  public static UserInfoResponseDto from(User user) {
    return new UserInfoResponseDto(
        user.getId(),
        user.getEmail(),
        user.getNickname(),
        user.getRole()
    );
  }
}
