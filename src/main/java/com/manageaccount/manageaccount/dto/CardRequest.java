package com.manageaccount.manageaccount.dto;

import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;

import java.sql.Timestamp;

public class CardRequest {
    @NotBlank(message = "Car cannot be empty")
    private String cardType;
    @NotBlank(message = "Customer name cannot be empty")
    private String status;

    public CardRequest(String cardType, String status) {
        this.cardType = cardType;
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
