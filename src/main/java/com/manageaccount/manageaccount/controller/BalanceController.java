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

    @PutMapping({"/{accountId}/subtractMoney"})
    public ResponseEntity<?> subtractMoney(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            this.balanceService.subtractMoneyFromAccount(accountId, amount);
            return ResponseEntity.status(HttpStatus.OK).body("Subtract successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
