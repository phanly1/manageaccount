package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.BalanceRequest;
import org.springframework.cache.annotation.Cacheable;
import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    @Autowired
    public BalanceRepository balanceRepository;
    @Autowired
    public AccountRepository accountRepository;

    @Cacheable(value = "balance", key = "#accountId")
    public Balance getBalance(Long accountId) {
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        return this.balanceRepository.findByAccountId(accountId);
    }

    @CachePut(value = "balance", key = "#balanceRequest.accountId")
    public void addMoneyToAccount(BalanceRequest balanceRequest) {
        Account account = this.accountRepository.findById(balanceRequest.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        Balance balance = this.balanceRepository.findByAccountId(balanceRequest.getAccountId());
        balance.setAvailableBalance(balance.getAvailableBalance().add(balanceRequest.getAmountAdded()));

        this.balanceRepository.save(balance);
    }

    @CachePut(value = "balance", key = "#balanceRequest.accountId")
    public void subtractMoneyFromAccount(BalanceRequest balanceRequest) {
        Account account = this.accountRepository.findById(balanceRequest.getAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        Balance balance = this.balanceRepository.findByAccountId(balanceRequest.getAccountId());

        if (balance.getAvailableBalance().compareTo(balanceRequest.getAmountSubtracted()) < 0) {
            throw new IllegalArgumentException("Don't subtract money");
        }

        balance.setAvailableBalance(balance.getAvailableBalance().subtract(balanceRequest.getAmountSubtracted()));
        this.balanceRepository.save(balance);
    }


//    public Balance getBalance(Long accountId) {
//        // Kiểm tra nếu dữ liệu có trong cache không
//        Balance balance = (Balance) redisTemplate.opsForValue().get("balance:" + accountId);
//
//        if (balance != null) {
//            // Nếu có trong cache, trả về từ cache
//            return balance;
//        }
//
//        // Nếu không có trong cache, lấy từ cơ sở dữ liệu
//        Account account = this.accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//        balance = this.balanceRepository.findByAccountId(accountId);
//
//        // Lưu vào cache và trả về
//        redisTemplate.opsForValue().set("balance:" + accountId, balance, 10, TimeUnit.MINUTES);
//        return balance;
//    }
//
//    public void addMoneyToAccount(Long accountId, BigDecimal amount) {
//        Account account = this.accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//
//        Balance balance = this.balanceRepository.findByAccountId(accountId);
//        balance.setAvailableBalance(balance.getAvailableBalance().add(amount));
//
//        this.balanceRepository.save(balance);
//
//        // Kiểm tra và cập nhật cache
//        Balance cachedBalance = (Balance) redisTemplate.opsForValue().get("balance:" + accountId);
//        if (cachedBalance != null) {
//            // Nếu có trong cache, cập nhật lại giá trị
//            cachedBalance.setAvailableBalance(balance.getAvailableBalance());
//            redisTemplate.opsForValue().set("balance:" + accountId, cachedBalance, 10, TimeUnit.MINUTES);
//        } else {
//            // Nếu không có trong cache, lưu trực tiếp vào cache
//            redisTemplate.opsForValue().set("balance:" + accountId, balance, 10, TimeUnit.MINUTES);
//        }
//    }
//
//
//    public void subtractMoneyFromAccount(Long accountId, BigDecimal amount) {
//        Account account = this.accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//
//        Balance balance = this.balanceRepository.findByAccountId(accountId);
//
//        if (balance.getAvailableBalance().compareTo(amount) < 0) {
//            throw new IllegalArgumentException("Don't subtract money");
//        }
//
//        balance.setAvailableBalance(balance.getAvailableBalance().subtract(amount));
//        this.balanceRepository.save(balance);
//
//        // Kiểm tra và cập nhật cache
//        Balance cachedBalance = (Balance) redisTemplate.opsForValue().get("balance:" + accountId);
//        if (cachedBalance != null) {
//            // Nếu có trong cache, cập nhật lại số dư
//            cachedBalance.setAvailableBalance(balance.getAvailableBalance());
//            redisTemplate.opsForValue().set("balance:" + accountId, cachedBalance, 10, TimeUnit.MINUTES);
//        } else {
//            // Nếu không có trong cache, lưu vào cache mới
//            redisTemplate.opsForValue().set("balance:" + accountId, balance, 10, TimeUnit.MINUTES);
//        }
//    }

}
