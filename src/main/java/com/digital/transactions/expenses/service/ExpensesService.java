package com.digital.transactions.expenses.service;

import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;

import java.util.List;
import java.util.UUID;

public interface ExpensesService {


    List<ExpensesDto> getExpensesByUserId(UUID userId) throws UserNotFoundException;
}
