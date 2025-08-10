package com.digital.transactions.expenses.controller;

import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.service.ExpensesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class ExpensesController {

    @Autowired
    ExpensesService expensesService;


    // Define endpoints for expenses-related operations here
    //for getting expenses by user
    @GetMapping("/api/expenses/{userId}")
    @PreAuthorize("hasRole('clientadmin')")
    public List<ExpensesDto> getExpenses(@PathVariable("userId") UUID userId) throws UserNotFoundException {
        return expensesService.getExpensesByUserId(userId);
    }
}
