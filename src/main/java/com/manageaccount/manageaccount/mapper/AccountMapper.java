package com.manageaccount.manageaccount.mapper;

import com.manageaccount.manageaccount.dto.AccountRequest;
import com.manageaccount.manageaccount.dto.AccountResponse;
import com.manageaccount.manageaccount.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account accountRequesttoAccount(AccountRequest accountRequest);

    AccountResponse accounttoAccountResponse(Account account);

    void updateAccountFromRequest( AccountRequest accountRequest, @MappingTarget Account account);
}
