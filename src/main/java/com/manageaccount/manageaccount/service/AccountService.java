package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.AccountPageDTO;
import com.manageaccount.manageaccount.dto.AccountRequest;
import com.manageaccount.manageaccount.dto.AccountResponse;
import jakarta.persistence.EntityExistsException;

public interface AccountService {

    AccountPageDTO getAccounts(int page, int size);

    AccountResponse createAccount(AccountRequest accountRequest) throws EntityExistsException;

    AccountResponse updateAccount(Long accountId, AccountRequest accountRequest);

    boolean canDeleteAccount(Long accountId);

    void deleteAccount(Long accountId);

    AccountResponse getAccountDetail(Long accountId);
}
