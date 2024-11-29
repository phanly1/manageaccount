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

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) throws EntityExistsException {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new EntityExistsException("Email is available");
        }

        account = accountRepository.save(account);

        Balance balance = new Balance();
        balance.setAvailableBalance(BigDecimal.ZERO);
        balance.setHoldBalance(BigDecimal.ZERO);
        balance.setAccountId(account.getAccountId());
        balanceRepository.save(balance);

        return account;
    }

    public Account updateAccount(Long accountId, Account accountDetails) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        account.setEmail(accountDetails.getEmail());
        account.setPhoneNumber(accountDetails.getPhoneNumber());

        return accountRepository.save(account);
    }

    public boolean canDeleteAccount(Long accountId) {
        Balance balance = balanceRepository.findByAccountId(accountId);
        if (balance != null && (balance.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0)) {
            return false;
        }

        List<Card> cards = cardRepository.findByAccountId(accountId);
        return cards.isEmpty();
    }

    public void deleteAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account dose not exist."));

        if (!canDeleteAccount(accountId)) {
            throw new IllegalArgumentException("Don't delete account");
        }
        accountRepository.delete(account);
    }

    public AccountDTO getAccountDTO(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account dose not exist"));

        List<Card> cards = cardRepository.findByAccountId(accountId);

        Balance balance = balanceRepository.findByAccountId(accountId);

        return new AccountDTO(
                account.getAccountId(),
                account.getCustomerName(),
                account.getEmail(),
                account.getPhoneNumber(),
                cards,
                balance
        );
    }
}
