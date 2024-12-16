package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.model.Card;

import java.util.List;

public class AccountDTO {
    private Long accountId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private List<Card> cards;
    private Balance balance;

    public AccountDTO(Long accountId, String customerName, String email, String phoneNumber, List<Card> cards, Balance balance) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.cards = cards;
        this.balance = balance;
    }
    public AccountDTO() {
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Balance getBalance() {
        return this.balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
