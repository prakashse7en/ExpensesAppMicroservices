package com.digital.transactions.expenses.pojo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "expenses")
public class Expenses {
    @Id
    @Column(name = "expenseId", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID expenseId;
    private UUID userId;
    private BigDecimal expenseAmount;
    private String category;
    private String description;

    @CreationTimestamp
    private Instant recordCreatedDateTime;

    @UpdateTimestamp
    private Instant recordUpdatedDateTime;
}
