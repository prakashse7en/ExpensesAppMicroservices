package com.digital.transactions.expenses.mapper;

import com.digital.transactions.expenses.pojo.entity.Expenses;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExpensesMapper {


    @Mapping(target = "expenseId", source = "expenseId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "expenseAmount", source = "expenseAmount")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "description", source = "description")
    ExpensesDto toExpensesDto(Expenses expenses);

    @Mapping(target = "expenseId", source = "expenseId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "expenseAmount", source = "expenseAmount")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "description", source = "description")
    Expenses toExpensesEntity(ExpensesDto expensesDto);


}
