package com.manageaccount.manageaccount.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageaccount.manageaccount.dto.PaymentDTO;
import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    public AccountRepository accountRepository;
    private static final String PAYMENT_QUEUE = "payment.queue"; // Tên Queue

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    public void sendPaymentMessage(PaymentDTO paymentDTO) throws Exception {
        // Gửi message vào Queue
        String paymentJson = objectMapper.writeValueAsString(paymentDTO);
        jmsTemplate.convertAndSend("payment.queue", paymentJson);
    }
}
