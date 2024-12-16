package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.CardRequest;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    public CardService cardService;

    @GetMapping
    public ResponseEntity<Object> getCardsByAccountId(@RequestParam Long accountId,
                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value="size", defaultValue = "10") int size) throws Exception{
            Page<Card> cards = this.cardService.getCardsByAccountId(accountId,page, size);
            return ResponseEntity.status(HttpStatus.OK).body(cards);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long accountId, @RequestBody CardRequest cardRequest)throws Exception {
            Card createCard = this.cardService.createCard(accountId, cardRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createCard);
    }

    @DeleteMapping({"/{cardId}"})
    public ResponseEntity<?> delete(@PathVariable Long cardId) throws Exception{
            this.cardService.deleteCardById(cardId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
