package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.entity.Account;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AccountPageDTO {
    // Getter and Setter
    private List<Account> accounts;
    private long totalElements;
    private int totalPages;

}
