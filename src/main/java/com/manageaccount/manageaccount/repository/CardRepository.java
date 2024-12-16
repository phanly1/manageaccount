package com.manageaccount.manageaccount.repository;

import com.manageaccount.manageaccount.entity.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Page<Card> findByAccountId(Long accountId, Pageable pageable);
    long countByAccountId(Long accountId);
}
