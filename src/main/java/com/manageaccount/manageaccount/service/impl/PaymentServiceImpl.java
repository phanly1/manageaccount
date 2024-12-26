package com.manageaccount.manageaccount.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageaccount.manageaccount.dto.BalanceRequest;
import com.manageaccount.manageaccount.dto.PaymentDTO;
import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
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
