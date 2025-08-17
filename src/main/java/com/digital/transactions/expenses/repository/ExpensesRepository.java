package com.digital.transactions.expenses.repository;

import com.digital.transactions.expenses.pojo.entity.Expenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, UUID> {


    @Query("SELECT e FROM Expenses e WHERE e.userId = :userId")
    Page<Expenses> findExpensesByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT e FROM Expenses e WHERE e.userId = :userId")
    Slice<Expenses> findExpensesByUserIdSliced(@Param("userId") UUID userId, Pageable pageable);

}
