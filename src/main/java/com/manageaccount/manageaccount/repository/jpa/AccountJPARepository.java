package com.manageaccount.manageaccount.repository.jpa;

import com.manageaccount.manageaccount.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountJPARepository extends JpaRepository<Account, Long> {
    boolean existsByEmail(String email);

    @Query(value = "Select a from Account a where a.accountId = 1L")
    Account getAccountByEmailAndCustomerName(String email);

    @Query(value = "SELECT u FROM Account u " +
            " WHERE u.customerName = :customerName AND " +
            " (u.email LIKE :email OR u.phoneNumber LIKE :phoneNumber) " +
            " ORDER BY u.accountId DESC",
            nativeQuery = false)// dung cho cac truong hop query phuc tap,nhung ma van can hibernate anh xa
    List<Account> findByNameQueryNativeQueryV(@Param("customerName") String customerName,
                                              @Param("email") String email,
                                              @Param("phoneNumber") String phoneNumber);

    @Query(value = "Select * from account where account_Id = 1L",nativeQuery = true) // chay truc tiep cau query ma k can hibernate anh xa
    Account getAccountByAccountId(Long accountId);


}
