package com.digital.transactions.expenses.repository;

import com.digital.transactions.expenses.pojo.entity.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpensesRepository extends JpaRepository<Expenses, String> {


    List<Expenses> findByUserId(UUID userId);
}
