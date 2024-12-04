package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.LoginRequest;
import com.manageaccount.manageaccount.dto.LoginResponse;
import com.manageaccount.manageaccount.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class JwtController {
    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Long accountId = loginRequest.getAccountId();
            if (accountId == null || !isValidAccountId(accountId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid accountId");
            }
            String token = jwtService.generateToken(accountId);
            return ResponseEntity.ok(new LoginResponse(token));
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating token");
        }
    }

    private boolean isValidAccountId(Long accountId) {
        return true;
    }


}
