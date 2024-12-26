package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.PaymentDTO;

public interface PaymentService {
    void sendPaymentMessage(PaymentDTO paymentDTO) throws Exception;

}
