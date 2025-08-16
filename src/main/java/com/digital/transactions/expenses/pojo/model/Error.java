package com.digital.transactions.expenses.pojo.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Error {
    private String error;
    private String message;

    public static Error fromErrorCode(ErrorCode errorCode) {
        return Error.builder()
                .error(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }
}