package com.digital.transactions.expenses.service;

import com.digital.transactions.expenses.exception.BusinessException;
import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.pojo.model.ExpensesDtoResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ExpensesService {

     ExpensesDtoResponse findExpensesByUserId(Pageable pageable, UUID userId) throws BusinessException;

     ExpensesDtoResponse findExpensesByUserIdSliced(Pageable pageable, UUID userId) throws BusinessException;

    ExpensesDto createExpenses(ExpensesDto expensesDto) throws UserNotFoundException;

}
