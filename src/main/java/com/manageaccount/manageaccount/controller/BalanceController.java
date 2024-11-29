package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.service.AccountService;
import com.manageaccount.manageaccount.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/balance")
public class BalanceController {
    @Autowired
    BalanceService balanceService;
    @Autowired
    AccountService accountService;

    @GetMapping({"/{accountId}"})
    public ResponseEntity<?> getBalance(@PathVariable Long accountId) {
        try {
            Balance balance = this.balanceService.getBalance(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(balance);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
