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

    public AccountController() {
    }

    @GetMapping
    public ResponseEntity<?> getAllAccount() {
        try {
            List<Account> accounts = this.accountService.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping({"{accountId}"})
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        try {
            AccountDTO accountDTO = this.accountService.getAccountDTO(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(accountDTO);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account account1 = this.accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(account1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping({"/{accountId}"})
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        try {
            this.accountService.deleteAccount(accountId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
