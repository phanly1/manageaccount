package com.manageaccount.manageaccount.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manageaccount.manageaccount.dto.PaymentDTO;
import com.manageaccount.manageaccount.repository.jpa.AccountJPARepository;
import com.manageaccount.manageaccount.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    public AccountJPARepository accountRepository;
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
