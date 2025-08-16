package com.digital.transactions.expenses.aop;

import com.digital.transactions.expenses.exception.BusinessException;
import com.digital.transactions.expenses.pojo.model.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class BusinessExceptionAdvice {

  @ExceptionHandler(BusinessException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  com.digital.transactions.expenses.pojo.model.Error UnexpectedErrorHandler(BusinessException ex) {

    return com.digital.transactions.expenses.pojo.model.Error.builder()
            .error(ErrorCode.BG1000.getCode())
            .message(ErrorCode.BG1000.getMessage())
            .build();
  }
}