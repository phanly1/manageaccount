package com.manageaccount.manageaccount.repository.jpa;

import com.manageaccount.manageaccount.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceJPARepository extends JpaRepository<Balance, Long> {
    Balance findByAccountId(Long accountId);
    void deleteByAccountId(Long accountId);
}
