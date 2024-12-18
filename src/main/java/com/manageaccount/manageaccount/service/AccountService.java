package com.manageaccount.manageaccount.service;

import com.manageaccount.manageaccount.dto.*;
import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
import com.manageaccount.manageaccount.entity.Card;
import com.manageaccount.manageaccount.repository.AccountRepository;
import com.manageaccount.manageaccount.repository.BalanceRepository;
import com.manageaccount.manageaccount.repository.CardRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CardService cardService;


    @Cacheable(value = "accounts", key = "#page + '-' + #size")
    public AccountPageDTO getAccounts(int page, int size) {
        Page<Account> result = accountRepository.findAll(PageRequest.of(page, size));

        AccountPageDTO accountPageDTO = new AccountPageDTO();
        accountPageDTO.setAccounts(result.getContent());
        accountPageDTO.setTotalElements(result.getTotalElements());
        accountPageDTO.setTotalPages(result.getTotalPages());

        return accountPageDTO;
    }

    public Account createAccount(CreateAccountRequest accountRequest) throws EntityExistsException {
        if (this.accountRepository.existsByEmail(accountRequest.getEmail())) {
            throw new EntityExistsException("Email is available");
        } else {
            Account account = new Account();
            account.setCustomerName(accountRequest.getCustomerName());
            account.setEmail(accountRequest.getEmail());
            account.setPhoneNumber(accountRequest.getPhoneNumber());
            account = accountRepository.save(account);

            Balance balance = new Balance();
            balance.setAvailableBalance(BigInteger.ZERO);
            balance.setHoldBalance(BigInteger.ZERO);
            balance.setAccountId(account.getAccountId());
            this.balanceRepository.save(balance);
            return account;
        }
    }

    @CachePut(value = "account", key = "#accountId")
    public Account updateAccount(Long accountId, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        account.setEmail(updateAccountRequest.getEmail());
        account.setPhoneNumber(updateAccountRequest.getPhoneNumber());
        this.accountRepository.save(account);
        // entityManager.merge(account);
        return account;

    }

    public boolean canDeleteAccount(Long accountId) {
        Balance balance = this.balanceRepository.findByAccountId(accountId);
        if (balance != null && balance.getAvailableBalance().compareTo(BigInteger.ZERO) > 0) {
            return false;
        } else {
            //kiểm tra xem tài khoản có thẻ nào không bằng cách sử dụng count
            long cardCount = this.cardRepository.countByAccountId(accountId);
            return cardCount == 0; // Nếu không có thẻ nào, trả về true
        }
    }

    @CacheEvict(value = "AccountResponse", key = "#accountId")
    public void deleteAccount(Long accountId) {
        if (!this.canDeleteAccount(accountId)) {
            throw new IllegalArgumentException("Don't delete account");
        }
        if (!this.accountRepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account does not exist.");
        }
        this.accountRepository.deleteById(accountId);
    }


    @Cacheable(value = "AccountResponse", key = "#accountId")
    public AccountResponse getAccountDetail(Long accountId) {
        Account account = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        // Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        CardPageDTO cardPageDTO = cardService.getCards(accountId, 0, Integer.MAX_VALUE);
        Balance balance = this.balanceRepository.findByAccountId(accountId);

        return new AccountResponse(account.getAccountId(), account.getCustomerName(), account.getEmail(), account.getPhoneNumber(), cardPageDTO, balance);
    }

    //    @Cacheable(value = "allAccounts", key = "#page + '-' + #size")
//    public Page<Account> getAccounts(int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);  // Tạo đối tượng Pageable từ page và size
//        Page<Account> result = accountRepository.findAll(pageable);  // Trả về một trang của danh sách tài khoản
//        return result;
//    }

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



