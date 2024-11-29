package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.model.Card;
import com.manageaccount.manageaccount.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    public CardService cardService;

    @GetMapping({"/{accountId}"})
    public ResponseEntity<?> getCardsByAccountId(@PathVariable Long accountId) {
        try {
            List<Card> cardList = this.cardService.getCardsByAccountId(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(cardList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
