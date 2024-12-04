package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.model.Card;
import com.manageaccount.manageaccount.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    public CardService cardService;

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getCardsByAccountId(@PathVariable Long accountId) {
        try {
            List<Card> cardList = cardService.getCardsByAccountId(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(cardList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> createCard(@PathVariable Long accountId, @RequestBody Card card) {
        try {

            Card createCard = cardService.createCard(accountId, card);
            return ResponseEntity.status(HttpStatus.CREATED).body(createCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        try {
            cardService.deleteCardById(cardId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
