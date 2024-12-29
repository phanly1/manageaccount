package com.manageaccount.manageaccount.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "Transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @JoinColumn(name = "accountId", nullable = false)
    private Long accountId;

    private Date transactionDate;
    private BigDecimal transactionAmount;
    private String transactionType;
    private String status;
    private String currency;
    private String location;


}
