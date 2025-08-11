package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.mapper.ExpensesMapper;
import com.digital.transactions.expenses.pojo.entity.Expenses;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.pojo.model.User;
import com.digital.transactions.expenses.repository.ExpensesRepository;
import com.digital.transactions.expenses.service.ExpensesService;
import com.digital.transactions.expenses.service.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;

@Component
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    ExpensesRepository expensesRepository;



    @Override
    public List<ExpensesDto> getExpensesByUserId(UUID userId) throws UserNotFoundException {
        List<ExpensesDto> expensesDtoList = null;
        try {

            User user = userProfileService.getUserProfileByUserId(userId);
            if (user == null) {

                throw new UserNotFoundException("user not found"); // or throw an exception
            }
            List<Expenses> expenses = expensesRepository.findByUserId(userId);

            if (!ObjectUtils.isEmpty(expenses)) {

                //stream list and map to dto
                ExpensesMapper expensesMapper = Mappers.getMapper(ExpensesMapper.class);

                expensesDtoList = expenses.stream()
                        .map(expense -> {
                            ExpensesDto expensesDto = expensesMapper.toDto(expense);
                            expensesDto.setUserName(user.getUserName());
                            return expensesDto;
                        })
                        .toList();


            }
            //convert all to expenses dto
        }catch(Exception e){
            e.printStackTrace();
            throw new UserNotFoundException("user not found");
        }
        return expensesDtoList;
    }
}
