package com.doneflow.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "할 일을 찾을 수 없습니다."),
  TODO_ALREADY_COMPLETED(HttpStatus.BAD_REQUEST, "이미 완료된 할 일입니다."),
  INVALID_TODO_CATEGORY(HttpStatus.BAD_REQUEST, "유효하지 않은 카테고리입니다."),

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
