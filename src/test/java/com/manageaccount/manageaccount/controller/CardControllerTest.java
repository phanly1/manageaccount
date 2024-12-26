package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.CardPageDTO;
import com.manageaccount.manageaccount.dto.CardRequest;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.service.impl.CardServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest extends GlobalSpringContext{
    @MockBean
    private CardServiceImpl cardService;

    //get cards
    @Test
    void getCardSuccess() throws Exception {
        Long accountId = 1L;
        int page = 1;
        int size = 2;

        CardPageDTO cardPageDTO = new CardPageDTO();
        cardPageDTO.setTotalPages(4);
        cardPageDTO.setTotalElements(8);
        cardPageDTO.setCards(new ArrayList<>());

        when(cardService.getCards(accountId,page,size)).thenReturn(cardPageDTO);

        mockMvc.perform(get("/cards")
                .param("accountId",String.valueOf(accountId))
                .param("page",String.valueOf(page))
                .param("size",String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("totalPages").value(4))
                .andExpect(jsonPath("totalElements").value(8))
                .andExpect(jsonPath("cards").value(cardPageDTO.getCards()))
                .andDo(MockMvcResultHandlers.print());
    }

    //Create Card
    @Test
    void createCardSuccess() throws Exception {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardType("credit");
        cardRequest.setStatus("active");

        Card card = new Card();
        card.setAccountId(1L);
        card.setCardId(1L);
        card.setCardType("credit");
       // Timestamp expiryDate = Timestamp.valueOf("2024-12-12 00:00:00");
       // card.setExpiryDate(expiryDate);
        card.setStatus("active");

        when(cardService.createCard(any(Long.class),any(CardRequest.class))).thenReturn(card);

        mockMvc.perform(post("/cards")
                .param("accountId",String.valueOf(1L))
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(cardRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("cardId").value(1L))
                .andExpect(content().json("{"
                        + "\"accountId\": 1,"
                        + "\"cardId\": 1,"
                        + "\"cardType\": \"credit\","
                  //      + "\"expiryDate\": \"1733936400000\","
                        + "\"status\": \"active\""
                        + "}"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void createCardFail() throws Exception {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardType("");
        mockMvc.perform(post("/cards"))
                .andExpect(status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    //delete cards
    @Test
    void deleteCardSuccess() throws Exception {
        Long cardId = 1L;
        doNothing().when(cardService).deleteCardById(cardId);

        mockMvc.perform(delete("/cards/{cardId}", cardId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void deleteCardFail() throws Exception {
        Long cardId = 1L;
        doThrow(new EntityNotFoundException()).when(cardService).deleteCardById(cardId);
        mockMvc.perform(delete("/cards/{cardId}", cardId))
                .andExpect(status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
}
