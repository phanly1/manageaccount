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

    public Balance getBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        return balanceRepository.findByAccountId(accountId);
    }

    public void addMoneyToAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        Balance balance = balanceRepository.findByAccountId(accountId);

        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));

        balanceRepository.save(balance);
    }

    public void subtractMoneyFromAccount(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        Balance balance = balanceRepository.findByAccountId(accountId);
        if (balance.getAvailableBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Don't subtract money");
        }

        balance.setAvailableBalance(balance.getAvailableBalance().subtract(amount));

        balanceRepository.save(balance);
    }
}
