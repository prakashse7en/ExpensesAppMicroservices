package com.digital.transactions.expenses.pojo.model;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data

public class ExpensesDtoResponse {
    private List<ExpensesDto> content;
    private boolean first;
    private boolean last;
    private boolean empty;
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private Pageable pageable;
    private boolean hasNext;
    private boolean hasPrevious;

}


