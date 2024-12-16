package com.manageaccount.manageaccount.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "Card")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
    private String cardType;
    private Timestamp expiryDate;
    private String status;

    @JoinColumn(name = "accountId", nullable = false)
    private Long accountId;
}
