package com.digital.transactions.expenses.pojo.model;

import lombok.Getter;

@Getter
public enum ErrorCode {
    BG1000("BG1000", "Unexpected error occurred."),
    BG1001("BG1001", "Invalid token."),
    ;
    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

}