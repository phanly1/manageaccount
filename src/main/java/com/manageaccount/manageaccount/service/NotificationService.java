package com.manageaccount.manageaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageaccount.manageaccount.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private ObjectMapper objectMapper;

    // Lắng nghe message từ payment.queue
    // @JmsListener(destination = "payment.queue")
    public void receivePayment(String paymentJson) throws Exception {
        PaymentDTO paymentDTO = objectMapper.readValue(paymentJson, PaymentDTO.class);
        System.out.println("Payment confirmed for paymentId: " + paymentDTO.getPaymentId());
        System.out.println("AccountId: " + paymentDTO.getAccountId());
        System.out.println("Amount: " + paymentDTO.getAmount());
        System.out.println("Currency: " + paymentDTO.getCurrency());
    }
}
