package com.digital.transactions.expenses.pojo.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ExpensesDto {
    private UUID expenseId;
    private UUID userId;
    private BigDecimal expenseAmount;
    private String category;
    private String description;
    private String userName;
}
