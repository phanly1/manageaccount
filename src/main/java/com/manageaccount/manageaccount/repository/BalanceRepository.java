package com.manageaccount.manageaccount.repository;

import com.manageaccount.manageaccount.model.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Balance findByAccountId(Long accountId);
}
