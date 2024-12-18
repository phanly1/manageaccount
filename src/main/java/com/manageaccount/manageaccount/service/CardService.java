package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.AccountPageDTO;
import com.manageaccount.manageaccount.dto.CardPageDTO;
import com.manageaccount.manageaccount.dto.CardRequest;
import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class CardService {
    @Autowired
    public AccountRepository accountRepository;
    @Autowired
    public CardRepository cardRepository;
    @Autowired
    public BalanceRepository balanceRepository;


    public CardPageDTO getCards(Long accountId, int page, int size) {
        Page<Card> result = cardRepository.findAll(PageRequest.of(page, size));

        CardPageDTO cardPageDTO = new CardPageDTO();
        cardPageDTO.setCards(result.getContent());
        cardPageDTO.setTotalPages(result.getTotalPages());
        cardPageDTO.setTotalElements(result.getTotalElements());

        return cardPageDTO;
    }

    public Card createCard(Long accountId, CardRequest cardRequest) {
        Account account = (Account) this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        Card card = new Card();
        card.setAccountId(accountId);
        card.setCardType(cardRequest.getCardType());

        LocalDateTime now = LocalDateTime.now();  // Lấy thời gian hiện tại
        LocalDateTime expiryDate = now.plusYears(2);  // Thêm 2 năm vào thời gian hiện tại
        Timestamp expiryTimestamp = Timestamp.valueOf(expiryDate);

        card.setExpiryDate(expiryTimestamp);
        card.setStatus(cardRequest.getStatus());
        return (Card) this.cardRepository.save(card);
    }

    public void deleteCardById(Long cardId) {
        Card card = (Card) this.cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        Balance balance = this.balanceRepository.findByAccountId(card.getAccountId());
        if (balance.getHoldBalance().compareTo(BigInteger.ZERO) > 0) {
            throw new IllegalArgumentException("Do not delete Card");
        } else {
            this.cardRepository.delete(card);
        }
    }

    //    public Page<Card> getCardsByAccountId(Long accountId, int page, int size) {
//        Account account = (Account) this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//        Pageable pageable = PageRequest.of(page, size);
//        return this.cardRepository.findByAccountId(accountId, pageable);
//    }
}

