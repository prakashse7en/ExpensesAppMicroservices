package com.digital.transactions.expenses.controller;

import com.digital.transactions.expenses.exception.BusinessException;
import com.digital.transactions.expenses.exception.UserNotFoundException;
import com.digital.transactions.expenses.pojo.model.ExpensesDto;
import com.digital.transactions.expenses.pojo.model.ExpensesDtoResponse;
import com.digital.transactions.expenses.service.ExpensesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ExpensesController {

    @Autowired
    ExpensesService expensesService;


    private final Logger logger = LoggerFactory.getLogger(ExpensesController.class);


    @PostMapping("/api/v1/expenses")
    @PreAuthorize("hasRole('clientadmin')")
    public ExpensesDto createExpense(@RequestBody ExpensesDto expensesDto) throws UserNotFoundException {
        try{
            return expensesService.createExpenses(expensesDto);
        }catch(Exception e) {
            logger.error("Error occurred while creating expense", e);
            throw e;
        }
    }

    /**
     * This endpoint returns a paginated list of expenses for a given user ID.
     * It allows pagination and sorting of the results.
     *
     * @param userId    The ID of the user whose expenses are to be retrieved.
     * @param page      The page number to retrieve (default is 0).
     * @param size      The number of records per page (default is 5).
     * @param sortBy    The field by which to sort the results (default is "category").
     * @param ascending Whether the results should be sorted in ascending order (default is true).
     * @return A paginated list of expenses for the specified user.
     * @throws UserNotFoundException If the user with the specified ID does not exist.
     */
    @GetMapping("/api/v1/expenses/{userId}")
    @PreAuthorize("hasRole('clientadmin')")
    public ExpensesDtoResponse getExpensesPaginated(@PathVariable("userId") UUID userId, @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size,
                                                    @RequestParam(defaultValue = "category") String sortBy,
                                                    @RequestParam(defaultValue = "true") boolean ascending) throws UserNotFoundException, BusinessException {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return expensesService.findExpensesByUserId(pageable,userId);
    }


    /**
     * This endpoint returns a slice of expenses for a given user ID.
     * It allows pagination and sorting of the results. no total elements count is returned.
     *
     * @param userId    The ID of the user whose expenses are to be retrieved.
     * @param page      The page number to retrieve (default is 0).
     * @param size      The number of records per page (default is 5).
     * @param sortBy    The field by which to sort the results (default is "category").
     * @param ascending Whether the results should be sorted in ascending order (default is true).
     * @return A slice of expenses for the specified user.
     * @throws UserNotFoundException If the user with the specified ID does not exist.
     */
    @GetMapping("/api/v2/expenses/{userId}")
    @PreAuthorize("hasRole('clientadmin')")
    public ExpensesDtoResponse getExpensesSliced(
            @PathVariable("userId") UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "category") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) throws UserNotFoundException, BusinessException {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return expensesService.findExpensesByUserIdSliced(pageable, userId);
    }
}
