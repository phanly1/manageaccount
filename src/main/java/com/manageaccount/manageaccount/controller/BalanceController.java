package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.BalanceRequest;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.service.AccountService;
import com.manageaccount.manageaccount.service.BalanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/balances")
public class BalanceController {
    @Autowired
    BalanceService balanceService;
    @Autowired
    AccountService accountService;

    @GetMapping
    public ResponseEntity<?> getBalance(@RequestParam Long accountId) throws Exception{
            Balance balance = this.balanceService.getBalance(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @PutMapping
    public ResponseEntity<?> processTransaction(@Valid @RequestBody BalanceRequest balanceRequest){
        balanceService.addMoneyToAccount(balanceRequest);
        balanceService.subtractMoneyFromAccount(balanceRequest);
        return ResponseEntity.status(HttpStatus.OK).body("successfully");
    }

    //    @PutMapping
//    public ResponseEntity<?> addMoney(@PathVariable Long accountId, @RequestBody )throws Exception {
//            Balance balance = balanceService.addMoneyToAccount(accountId, amount);
//            return ResponseEntity.status(HttpStatus.OK).body("Add successfully");
//    }

//    @PutMapping({"/{accountId}/subtractMoney"})
//    public ResponseEntity<?> subtractMoney(@PathVariable Long accountId, @RequestParam BigDecimal amount) throws Exception{
//            Balance balance = balanceService.subtractMoneyFromAccount(accountId, amount);
//            return ResponseEntity.status(HttpStatus.OK).body("Subtract successfully");
//    }
}
