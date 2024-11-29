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

    @PostMapping({"/{accountId}"})
    public ResponseEntity<?> createCard(@PathVariable Long accountId, @RequestBody Card card) {
        try {
            Card createCard = this.cardService.createCard(accountId, card);
            return ResponseEntity.status(HttpStatus.CREATED).body(createCard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
