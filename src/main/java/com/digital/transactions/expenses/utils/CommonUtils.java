package com.digital.transactions.expenses.utils;

import com.digital.transactions.expenses.pojo.entity.Expenses;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.pojo.model.ExpensesDtoResponse;
import com.digital.transactions.expenses.pojo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.UUID;

public class CommonUtils {

    public static void optimizeUserNameForDefaultUser(UUID userId, User user) {
        if(user.getUserName().equalsIgnoreCase("DEFAULT_USER")){
            user.setUserName(user.getUserName()+ userId);
        }
    }

    public static ExpensesDtoResponse mapExpensesDtoResponse(Pageable pageable,  List<ExpensesDto> expensesDto, Page<Expenses> expenses) {
        ExpensesDtoResponse expensesDtoResponse = new ExpensesDtoResponse();
        expensesDtoResponse.setContent(expensesDto);
        expensesDtoResponse.setSize(expenses.getSize());
        expensesDtoResponse.setTotalElements(expenses.getTotalElements());
        expensesDtoResponse.setTotalPages(expenses.getTotalPages());
        expensesDtoResponse.setPageable(pageable);
        expensesDtoResponse.setHasNext(expenses.hasNext());
        expensesDtoResponse.setHasPrevious(expenses.hasPrevious());
        return expensesDtoResponse;
    }



    public static ExpensesDtoResponse mapExpensesDtoResponseSliced(ExpensesDtoResponse expensesDtoResponse, List<ExpensesDto> expensesDto, Slice<Expenses> expenses) {
        expensesDtoResponse = new ExpensesDtoResponse();
        expensesDtoResponse.setContent(expensesDto);
        expensesDtoResponse.setHasNext(expenses.hasNext());
        expensesDtoResponse.setHasPrevious(expenses.hasPrevious());
        return expensesDtoResponse;
    }
}
