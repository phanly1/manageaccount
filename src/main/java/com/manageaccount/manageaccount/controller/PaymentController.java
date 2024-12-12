package com.manageaccount.manageaccount.controller;

import com.manageaccount.manageaccount.dto.PaymentDTO;
import com.manageaccount.manageaccount.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentDTO paymentDTO){
       try {
           paymentService.sendPaymentMessage(paymentDTO);
           return ResponseEntity.status(HttpStatus.OK).body( "Payment request for paymentId " + paymentDTO.getPaymentId() + "has been sent to the queue");
       }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }
}
