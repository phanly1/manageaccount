package com.manageaccount.manageaccount.repository;

import com.manageaccount.manageaccount.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByAccountId(Long accountId);
}
