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

    @PutMapping({"/{accountId}/addMoney"})
    public ResponseEntity<?> addMoney(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            this.balanceService.addMoneyToAccount(accountId, amount);
            return ResponseEntity.status(HttpStatus.OK).body("Add successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
