package com.manageaccount.manageaccount.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Setter
@Getter
public class BalanceRequest {
    private Long accountId;
    @NotNull
    @PositiveOrZero
    private BigInteger amountAdded;
    @NotNull
    @PositiveOrZero
    private BigInteger amountSubtracted;

    public BalanceRequest(Long accountId, BigInteger amountAdded, BigInteger amountSubtracted) {
        this.accountId = accountId;
        this.amountAdded = amountAdded;
        this.amountSubtracted = amountSubtracted;
    }


}
