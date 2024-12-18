package com.manageaccount.manageaccount.dto;

import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.entity.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Long accountId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private CardPageDTO cardPageDTO;
    private Balance balance;
}
