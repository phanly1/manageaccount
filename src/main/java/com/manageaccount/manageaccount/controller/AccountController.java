package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.AccountDTO;
import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import com.manageaccount.manageaccount.service.AccountService;
import com.manageaccount.manageaccount.service.BalanceService;
import com.manageaccount.manageaccount.service.CardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private CardService cardService;
    @Autowired
    private BalanceService balanceService;

    @PutMapping({"/{accountId}"})
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable Long accountId) {
        try {
            Account accountUpdate = this.accountService.updateAccount(accountId, account);
            return ResponseEntity.status(HttpStatus.OK).body(accountUpdate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
