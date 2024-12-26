package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.CardPageDTO;
import com.manageaccount.manageaccount.dto.CardRequest;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.service.impl.CardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    public CardServiceImpl cardService;

    //    @GetMapping
//    public ResponseEntity<Object> getCardsByAccountId(@RequestParam Long accountId,
//                                                      @RequestParam(value = "page", defaultValue = "0") int page,
//                                                      @RequestParam(value="size", defaultValue = "10") int size) throws Exception{
//            Page<Card> cards = this.cardService.getCardsByAccountId(accountId,page, size);
//            return ResponseEntity.status(HttpStatus.OK).body(cards);
//    }
    @GetMapping
    public ResponseEntity<CardPageDTO> getCardsByAccountId(@RequestParam Long accountId,
                                                           @RequestParam(value = "page", defaultValue = "0") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
        CardPageDTO cardPageDTO = this.cardService.getCards(accountId, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(cardPageDTO);
    }


    @PostMapping
    public ResponseEntity<?> create(@RequestParam Long accountId, @RequestBody CardRequest cardRequest) throws Exception {
        Card createCard = this.cardService.createCard(accountId, cardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCard);
    }

    @DeleteMapping({"/{cardId}"})
    public ResponseEntity<?> delete(@PathVariable Long cardId) throws Exception {
        this.cardService.deleteCardById(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
