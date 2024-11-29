package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.model.Card;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CardService {
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public BalanceRepository balanceRepository;

    public Card createCard(Long accountId, Card card) {
        Account account = (Account)this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        card.setAccountId(accountId);
        return (Card)this.cardRepository.save(card);
    }
}
