package com.digital.transactions.expenses.aop;


import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class UserNotFoundAdvice {

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  com.digital.transactions.expenses.pojo.model.Error  UnexpectedErrorHandler(UserNotFoundException ex) {

    return com.digital.transactions.expenses.pojo.model.Error.builder()
            .error(ErrorCode.BG1002.getCode())
            .message(ErrorCode.BG1002.getMessage())
            .build();
  }
}