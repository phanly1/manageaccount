package com.manageaccount.manageaccount.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.math.BigInteger;

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

    public BigInteger getAmountAdded() {
        return amountAdded;
    }

    public void setAmountAdded(BigInteger amountAdded) {
        this.amountAdded = amountAdded;
    }

    public BigInteger getAmountSubtracted() {
        return amountSubtracted;
    }

    public void setAmountSubtracted(BigInteger amountSubtracted) {
        this.amountSubtracted = amountSubtracted;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }


}
