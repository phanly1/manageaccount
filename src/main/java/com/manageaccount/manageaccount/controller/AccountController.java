package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.*;
import com.manageaccount.manageaccount.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    public AccountController(AccountServiceImpl accountService) {
    }


    @PutMapping({"/{accountId}"})
    public ResponseEntity<?> update(@Valid @RequestBody AccountRequest accountRequest,  @PathVariable @NotNull(message = "Account ID cannot be null") Long accountId) throws Exception {
            AccountResponse accountResponse = this.accountService.updateAccount(accountId, accountRequest);
            return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AccountRequest accountRequest)  throws Exception {
        AccountResponse accountResponse = this.accountService.createAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(accountResponse);
    }


    public AccountController() {
    }

    //    @GetMapping
//    public ResponseEntity<Object> getAccounts(
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) throws Exception {
//        Page<Account> accounts = this.accountService.getAccounts(page, size);
//        return ResponseEntity.status(HttpStatus.OK).body(accounts);
//    }
    @GetMapping
    public ResponseEntity<AccountPageDTO> getAccounts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "2") int size) {
        AccountPageDTO accountPageDTO = accountService.getAccounts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(accountPageDTO);
    }

    @GetMapping({"/{accountId}"})
    public ResponseEntity<?> getAccount(@PathVariable @NotNull Long accountId) throws Exception{
        AccountResponse accountResponse = this.accountService.getAccountDetail(accountId);
        return ResponseEntity.status(HttpStatus.OK).body(accountResponse);
    }



    @DeleteMapping({"/{accountId}"})
    public ResponseEntity<?> delete(@PathVariable @NotNull(message = "Account ID cannot be null") Long accountId) throws Exception {
        this.accountService.deleteAccount(accountId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
