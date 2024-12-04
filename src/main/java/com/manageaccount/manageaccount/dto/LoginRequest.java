package com.manageaccount.manageaccount.dto;

public class LoginRequest {
    private Long accountId;

    public LoginRequest(Long accountId) {
        this.accountId = accountId;
    }
    public LoginRequest() {}

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
