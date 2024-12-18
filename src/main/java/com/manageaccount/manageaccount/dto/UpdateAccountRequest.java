package com.manageaccount.manageaccount.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateAccountRequest {
    @NotBlank(message = "Customer name cannot be empty")
    private Long accountId;
    @NotBlank(message = "Customer name cannot be empty")
    private String customerName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number cannot be empty")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;

    public UpdateAccountRequest(Long accountId, String customerName, String email, String phoneNumber) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static class BalanceRequest {
    }
}
