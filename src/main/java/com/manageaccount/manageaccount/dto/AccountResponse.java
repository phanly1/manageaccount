package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.entity.Card;
import org.springframework.data.domain.Page;

public class AccountResponse {
    private Long accountId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private Page<Card> cards;
    private Balance balance;

    public AccountResponse(Long accountId, String customerName, String email, String phoneNumber, Page<Card> cards, Balance balance) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cards = cards;
        this.balance = balance;
    }
    public AccountResponse() {}
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Page<Card> getCards() {
        return cards;
    }

    public void setCards(Page<Card> cards) {
        this.cards = cards;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public static class BalanceRequest {
    }
}
