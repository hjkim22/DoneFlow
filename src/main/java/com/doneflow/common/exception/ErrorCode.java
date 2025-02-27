package com.doneflow.common.exception;

import static org.springframework.http.HttpStatus.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
  INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),

  TODO_NOT_FOUND(NOT_FOUND, "할 일을 찾을 수 없습니다."),
  TODO_ALREADY_COMPLETED(BAD_REQUEST, "이미 완료된 할 일입니다."),

  INVALID_REPEAT_COUNT(BAD_REQUEST, "반복 횟수는 1 이상이어야 합니다."),
  INVALID_REPEAT_CONFIGURATION(BAD_REQUEST, "반복 유형이 없는데 반복 횟수가 설정되었습니다."),
  REPEAT_TODO_REQUIRES_DUE_DATE(BAD_REQUEST, "반복 할 일은 마감 기한이 필요합니다."),
  UNSUPPORTED_REPEAT_TYPE(BAD_REQUEST, "지원되지 않는 반복 유형입니다."),

  INVALID_TODO_CATEGORY(BAD_REQUEST, "유효하지 않은 카테고리입니다."),
  CATEGORY_NOT_FOUND(NOT_FOUND, "카테고리를 찾을 수 없습니다."),
  CATEGORY_ALREADY_EXISTS(BAD_REQUEST, "이미 존재하는 카테고리입니다."),
  CANNOT_DELETE_DEFAULT_CATEGORY(BAD_REQUEST, "기본 카테고리는 삭제할 수 없습니다."),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
  UNAUTHORIZED_ACCESS(UNAUTHORIZED, "접근 권한이 없습니다."),
  INVALID_REQUEST(BAD_REQUEST, "잘못된 요청입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
