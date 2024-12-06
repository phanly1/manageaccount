package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.AccountDTO;
import com.manageaccount.manageaccount.model.Account;
import com.manageaccount.manageaccount.model.Balance;
import com.manageaccount.manageaccount.model.Card;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "allAccounts", unless = "#result == null")
    public List<Account> getAllAccounts() {
        // Nếu dữ liệu có trong cache, Spring tự động trả về mà không cần truy vấn cơ sở dữ liệu
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) throws EntityExistsException {
        if (this.accountRepository.existsByEmail(account.getEmail())) {
            throw new EntityExistsException("Email is available");
        } else {
            account = (Account) this.accountRepository.save(account);
            Balance balance = new Balance();
            balance.setAvailableBalance(BigDecimal.ZERO);
            balance.setHoldBalance(BigDecimal.ZERO);
            balance.setAccountId(account.getAccountId());
            this.balanceRepository.save(balance);
            return account;
        }
    }

    @CachePut(value = "account", key = "#accountId")
    public Account updateAccount(Long accountId, Account accountDetails) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        account.setEmail(accountDetails.getEmail());
        account.setPhoneNumber(accountDetails.getPhoneNumber());
        this.accountRepository.save(account);

        // Cập nhật cache nếu có
        redisTemplate.opsForValue().set("account:" + accountId, account, 10, TimeUnit.MINUTES);
        return account;

    }

    public boolean canDeleteAccount(Long accountId) {
        Balance balance = this.balanceRepository.findByAccountId(accountId);
        if (balance != null && balance.getAvailableBalance().compareTo(BigDecimal.ZERO) > 0) {
            return false;
        } else {
            List<Card> cards = this.cardRepository.findByAccountId(accountId);
            return cards.isEmpty();
        }
    }

    @CacheEvict(value = "AccountDTO", key = "#accountId")
    public void deleteAccount(Long accountId) {
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist."));

        if (!this.canDeleteAccount(accountId)) {
            throw new IllegalArgumentException("Don't delete account");
        }

        // Xóa tài khoản khỏi cơ sở dữ liệu
        this.accountRepository.delete(account);
    }


    @Cacheable(value = "AccountDTO", key = "#accountId")
    public AccountDTO getAccountDTO(Long accountId) {
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        List<Card> cards = this.cardRepository.findByAccountId(accountId);
        Balance balance = this.balanceRepository.findByAccountId(accountId);

        return  new AccountDTO(account.getAccountId(), account.getCustomerName(), account.getEmail(), account.getPhoneNumber(), cards, balance);
    }

//    public Account updateAccount(Long accountId, Account accountDetails) {
//        Account account = accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//        account.setEmail(accountDetails.getEmail());
//        account.setPhoneNumber(accountDetails.getPhoneNumber());
//        this.accountRepository.save(account);
//
//        // Cập nhật cache nếu có
//        redisTemplate.opsForValue().set("account:" + accountId, account, 10, TimeUnit.MINUTES);
//        return account;
//
//    }


//    public AccountDTO getAccountDTO(Long accountId) {
//        // Kiểm tra xem dữ liệu đã có trong cache chưa
//        AccountDTO cachedAccountDTO = (AccountDTO) redisTemplate.opsForValue().get("account:" + accountId);
//
//        if (cachedAccountDTO != null) {
//            // Nếu đã có trong cache, trả về từ cache
//            return cachedAccountDTO;
//        }
//
//        // Nếu không có trong cache, lấy từ cơ sở dữ liệu
//        Account account = this.accountRepository.findById(accountId)
//                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
//
//        List<Card> cards = this.cardRepository.findByAccountId(accountId);
//        Balance balance = this.balanceRepository.findByAccountId(accountId);
//        AccountDTO accountDTO = new AccountDTO(account.getAccountId(), account.getCustomerName(), account.getEmail(), account.getPhoneNumber(), cards, balance);
//
//        // Lưu kết quả vào cache với TTL 10 phút
//        redisTemplate.opsForValue().set("account:" + accountId, accountDTO, 10, TimeUnit.MINUTES);
//
//        return accountDTO;
//    }

//    public List<Account> getAllAccounts() {
//        // Kiểm tra xem dữ liệu đã có trong cache hay chưa
//        Object cachedAccounts = redisTemplate.opsForValue().get("All account");
//
//        if (cachedAccounts != null) {
//            // Nếu dữ liệu đã có trong cache, trả về ngay
//            return (List<Account>) cachedAccounts;
//        }
//
//        // Nếu không có trong cache, truy vấn cơ sở dữ liệu
//        List<Account> accounts = accountRepository.findAll();
//
//        // Lưu dữ liệu vào cache Redis với một thời gian sống (TTL) nhất định (ví dụ 1 giờ)
//        redisTemplate.opsForValue().set("All account:", accounts, 10, TimeUnit.MINUTES);  // 3600 giây = 1 giờ
//
//        return accounts;
//    }

//    public void deleteAccount(Long accountId) {
//        Account account = (Account) this.accountRepository.findById(accountId).orElseThrow(() -> new EntityNotFoundException("Account dose not exist."));
//        if (!this.canDeleteAccount(accountId)) {
//            throw new IllegalArgumentException("Don't delete account");
//        } else {
//            redisTemplate.delete("account:" + accountId);
//            this.accountRepository.delete(account);
//        }
//    }

}



