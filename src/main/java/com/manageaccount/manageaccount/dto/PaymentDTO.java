package com.manageaccount.manageaccount.dto;

import java.io.Serializable;

public class PaymentDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String paymentId;
    private Long accountId;
    private double amount;
    private String currency;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentDTO(String paymentId, Long accountId, String currency, double amount) {
        this.paymentId = paymentId;
        this.accountId = accountId;
        this.currency = currency;
        this.amount = amount;
    }
}
