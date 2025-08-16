package com.digital.transactions.expenses.service.impl;

import com.digital.transactions.expenses.exception.BusinessException;
import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.mapper.ExpensesMapper;
import com.digital.transactions.expenses.pojo.entity.Expenses;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.pojo.model.ExpensesDtoResponse;
import com.digital.transactions.expenses.pojo.model.User;
import com.digital.transactions.expenses.repository.ExpensesRepository;
import com.digital.transactions.expenses.service.ExpensesService;
import com.digital.transactions.expenses.service.UserProfileService;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.digital.transactions.expenses.utils.CommonUtils.*;

@Service
public class ExpensesServiceImpl implements ExpensesService {

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    ExpensesRepository expensesRepository;

    private final Logger logger = LoggerFactory.getLogger(ExpensesServiceImpl.class);


    @Override
    public ExpensesDtoResponse findExpensesByUserId(Pageable pageable, UUID userId) throws BusinessException {
        Page<Expenses> expenses =null;
        ExpensesDtoResponse expensesDtoResponse =null;
        try{
            User user = userProfileService.getUserProfileByUserId(userId);
            if (user == null) {
                throw new UserNotFoundException("user not found"); // or throw an exception
            }

            optimizeUserNameForDefaultUser(userId, user);

            expenses = expensesRepository.findExpensesByUserId(userId,pageable);
            // Convert the Page<Expenses> to a List<ExpensesDto>
            ExpensesMapper expensesMapper = Mappers.getMapper(ExpensesMapper.class);


            List<ExpensesDto> expensesDto = expenses.stream().map(expense -> {
                ExpensesDto dto = expensesMapper.toExpensesDto(expense);
                dto.setUserName(user.getUserName()); // Set the additional value
                return dto;
            }).collect(Collectors.toList());

            expensesDtoResponse =mapExpensesDtoResponse(pageable,  expensesDto, expenses);

        }catch(Exception e){
            logger.error("error occurred",e);
            throw new BusinessException("Unexpected Error occurred");
        }
        return expensesDtoResponse;
    }




    public ExpensesDtoResponse findExpensesByUserIdSliced(Pageable pageable, UUID userId) throws BusinessException {
        Slice<Expenses> expenses = null;
        ExpensesDtoResponse expensesDtoResponse =null;
        try {
            User user = userProfileService.getUserProfileByUserId(userId);
            if (user == null) {
                throw new UserNotFoundException("user not found"); // or throw an exception
            }

            optimizeUserNameForDefaultUser(userId, user);

            expenses = expensesRepository.findExpensesByUserIdSliced(userId, pageable);
            // Convert the Page<Expenses> to a List<ExpensesDto>
            ExpensesMapper expensesMapper = Mappers.getMapper(ExpensesMapper.class);


            List<ExpensesDto> expensesDto = expenses.stream().map(expense -> {
                ExpensesDto dto = expensesMapper.toExpensesDto(expense);
                dto.setUserName(user.getUserName()); // Set the additional value
                return dto;
            }).collect(Collectors.toList());
            expensesDtoResponse =mapExpensesDtoResponseSliced(expensesDtoResponse, expensesDto, expenses);

        } catch (Exception e) {
            logger.error("error occurred", e);
            throw new BusinessException("Error occurred while fetching expenses");
        }
        return expensesDtoResponse;
    }



    @Override
    public ExpensesDto createExpenses(ExpensesDto expensesDto) throws UserNotFoundException {

        try{
            User user = userProfileService.getUserProfileByUserId(expensesDto.getUserId());
            if (user == null) {
                throw new UserNotFoundException("user not found"); // or throw an exception
            }
            optimizeUserNameForDefaultUser(expensesDto.getUserId(), user);
            Expenses expenses = Mappers.getMapper(ExpensesMapper.class).toExpensesEntity(expensesDto);
            expenses = expensesRepository.save(expenses);
            expensesDto = Mappers.getMapper(ExpensesMapper.class).toExpensesDto(expenses);
            expensesDto.setUserName(user.getUserName());
            return expensesDto;

        }catch (Exception e){
            logger.error("Error occurred while creating expense", e);
            throw e;
        }

    }


}
