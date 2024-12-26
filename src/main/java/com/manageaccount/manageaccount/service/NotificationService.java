package com.manageaccount.manageaccount.service;

public interface NotificationService {
    void receivePayment(String paymentJson) throws Exception;
}
