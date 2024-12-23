package com.manageaccount.manageaccount.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.math.BigInteger;

@Getter
@Setter
@Entity
@Table(name = "Balance")
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    private BigInteger availableBalance;
    private BigInteger holdBalance;


    @JoinColumn(name = "accountId", nullable = false)
    private Long accountId;

}
