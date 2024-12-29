package com.manageaccount.manageaccount.service.impl;

import com.manageaccount.manageaccount.dto.*;
import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
//import com.manageaccount.manageaccount.mapper.AccountMapper;
import com.manageaccount.manageaccount.mapper.AccountMapper;
import com.manageaccount.manageaccount.repository.jdbc.AccountJDBCRepository;
import com.manageaccount.manageaccount.repository.jpa.AccountJPARepository;
import com.manageaccount.manageaccount.repository.jpa.BalanceJPARepository;
import com.manageaccount.manageaccount.repository.jpa.CardJPARepository;
import com.manageaccount.manageaccount.service.AccountService;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountJPARepository accountJPARepository;
    @Autowired
    private CardJPARepository cardJPARepository;
    @Autowired
    private BalanceJPARepository balanceJPARepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CardServiceImpl cardService;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private AccountJDBCRepository accountJDBCRepository;

    @Cacheable(value = "accounts", key = "#page + '-' + #size")
    public AccountPageDTO getAccounts(int page, int size) {
        Page<Account> result = accountJPARepository.findAll(PageRequest.of(page, size));

        AccountPageDTO accountPageDTO = new AccountPageDTO();
        accountPageDTO.setAccounts(result.getContent());
        accountPageDTO.setTotalElements(result.getTotalElements());
        accountPageDTO.setTotalPages(result.getTotalPages());

        return accountPageDTO;
    }

    @Transactional(rollbackFor = EntityNotFoundException.class) //khi cos
    public AccountResponse createAccount(AccountRequest accountRequest) throws EntityExistsException {
        if (this.accountJPARepository.existsByEmail(accountRequest.getEmail())) {
            throw new EntityExistsException("Email already exists");
        } else {
            Account account = accountMapper.accountRequesttoAccount(accountRequest);
            //this.accountJPARepository.save(account);
            account.setAccountId(accountJDBCRepository.saveAccount(account));

            Balance balance = new Balance();
            balance.setAvailableBalance(BigInteger.ZERO);
            balance.setHoldBalance(BigInteger.ZERO);
            balance.setAccountId(account.getAccountId());
            this.balanceJPARepository.save(balance);

            return accountMapper.accounttoAccountResponse(account);
        }
    }

    @CachePut(value = "account", key = "#accountId")
    @Transactional
    public AccountResponse updateAccount(Long accountId, AccountRequest accountRequest) {
        Account account = accountJPARepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        accountMapper.updateAccountFromRequest(accountRequest, account);
        //this.accountJPARepository.save(account);
        // entityManager.merge(account);
        return accountMapper.accounttoAccountResponse(accountJDBCRepository.updateAccount(account));
    }

    public boolean canDeleteAccount(Long accountId) {
        Balance balance = this.balanceJPARepository.findByAccountId(accountId);
        if (balance != null && balance.getAvailableBalance().compareTo(BigInteger.ZERO) > 0) {
            return false;
        } else {
            //kiểm tra xem tài khoản có thẻ nào không bằng cách sử dụng count
            long cardCount = this.cardJPARepository.countByAccountId(accountId);
            return cardCount == 0; // Nếu không có thẻ nào, trả về true
        }
    }

    @CacheEvict(value = "AccountResponse", key = "#accountId")
    @Transactional
    public void deleteAccount(Long accountId) {
        if (!this.canDeleteAccount(accountId)) {
            throw new IllegalArgumentException("Don't delete account");
        }
        if (!this.accountJPARepository.existsById(accountId)) {
            throw new EntityNotFoundException("Account does not exist.");
        }
//        this.accountJPARepository.deleteById(accountId);
//        this.balanceJPARepository.deleteByAccountId(accountId);

        accountJDBCRepository.deleteAccount(accountId);
        balanceJPARepository.deleteByAccountId(accountId);
    }


    @Cacheable(value = "AccountResponse", key = "#accountId")
    public AccountResponse getAccountDetail(Long accountId) {
        Account account = this.accountJPARepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Account does not exist"));
        // Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);
        CardPageDTO cardPageDTO = cardService.getCards(accountId, 0, Integer.MAX_VALUE);
        Balance balance = this.balanceJPARepository.findByAccountId(accountId);

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



