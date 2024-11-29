package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {
    @Autowired
    public BalanceRepository balanceRepository;
    @Autowired
    public AccountRepository accountRepository;

    public void addMoneyToAccount(Long accountId, BigDecimal amount) {
        Account account = (Account)this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        Balance balance = this.balanceRepository.findByAccountId(accountId);
        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
        this.balanceRepository.save(balance);
    }
}
