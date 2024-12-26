package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.CardPageDTO;
import com.manageaccount.manageaccount.dto.CardRequest;
import com.manageaccount.manageaccount.entity.Card;

public interface CardService {
    CardPageDTO getCards(Long accountId, int page, int size);

    Card createCard(Long accountId, CardRequest cardRequest);

    void deleteCardById(Long cardId);

}
