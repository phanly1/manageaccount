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
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping
    public ResponseEntity<?> getAllAccount() {
        try {
            List<Account> accounts = accountService.getAllAccounts();
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            Account account1 = accountService.createAccount(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(account1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@RequestBody Account account, @PathVariable Long accountId) {
        try {
            Account accountUpdate = accountService.updateAccount(accountId, account);
            return ResponseEntity.status(HttpStatus.OK).body(accountUpdate);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        try {
            accountService.deleteAccount(accountId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountId) {
        try {
            AccountDTO accountDTO = accountService.getAccountDTO(accountId);
            return ResponseEntity.status(HttpStatus.OK).body(accountDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> getLoggedInAccount() {
        try {
            // Lấy đối tượng Authentication từ SecurityContext
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Kiểm tra xem Authentication có tồn tại và principal có phải là một giá trị hợp lệ
            if (authentication == null || !(authentication.getPrincipal() instanceof Long)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid authentication principal");
            }

            Long accountId = (Long) authentication.getPrincipal();
            AccountDTO accountDTO = accountService.getAccountDTO(accountId);
            if (accountDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
            }
            return ResponseEntity.status(HttpStatus.OK).body(accountDTO);

        } catch (ClassCastException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Unable to cast principal to Long");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }
}

