package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.BalanceRequest;
import com.manageaccount.manageaccount.entity.Balance;

public interface BalanceService {
    Balance getBalance(Long accountId);

    void addMoneyToAccount(BalanceRequest balanceRequest);

    void subtractMoneyFromAccount(BalanceRequest balanceRequest);
}
