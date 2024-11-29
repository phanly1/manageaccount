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

    public List<Card> getCardsByAccountId(Long accountId) {
        Account account = (Account) this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        return this.cardRepository.findByAccountId(accountId);
    }

    public Card createCard(Long accountId, Card card) {
        Account account = (Account) this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        card.setAccountId(accountId);
        return (Card) this.cardRepository.save(card);
    }

    public void deleteCardById(Long cardId) {
        Card card = (Card) this.cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        Balance balance = this.balanceRepository.findByAccountId(card.getAccountId());
        if (balance.getHoldBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Do not delete Card");
        } else {
            this.cardRepository.delete(card);
        }
    }
}
