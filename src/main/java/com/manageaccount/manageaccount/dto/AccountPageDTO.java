package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.entity.Account;

import java.util.List;

public class AccountPageDTO {
    private List<Account> accounts;
    private long totalElements;
    private int totalPages;

    // Getter and Setter
    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
