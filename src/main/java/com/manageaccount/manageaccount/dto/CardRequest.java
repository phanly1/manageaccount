package com.manageaccount.manageaccount.dto;

import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class CardRequest {
    @NotBlank(message = "Car cannot be empty")
    private String cardType;
    @NotBlank(message = "Customer name cannot be empty")
    private String status;

    public CardRequest(String cardType, String status) {
        this.cardType = cardType;
        this.status = status;
    }

}
