package com.manageaccount.manageaccount.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class PaymentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private Long accountId;
    private double amount;
    private String currency;

    public PaymentDTO(String paymentId, Long accountId, String currency, double amount) {
        this.paymentId = paymentId;
        this.accountId = accountId;
        this.currency = currency;
        this.amount = amount;
    }
}
