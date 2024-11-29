package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.AccountDTO;
import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.model.Card;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private BalanceRepository balanceRepository;

    public boolean canDeleteAccount(Long accountId) {
        Balance balance = this.balanceRepository.findByAccountId(accountId);
        if (balance != null && balance.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
            return false;
        } else {
            List<Card> cards = this.cardRepository.findByAccountId(accountId);
            return cards.isEmpty();
        }
    }

    public void deleteAccount(Long accountId) {
        Account account = (Account)this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account dose not exist."));
        if (!this.canDeleteAccount(accountId)) {
            throw new IllegalArgumentException("Don't delete account");
        } else {
            this.accountRepository.delete(account);
        }
    }
}
