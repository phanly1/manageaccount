package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardPageDTO {
    private List<Card> cards;
    private long totalElements;
    private int totalPages;
}
