package com.manageaccount.manageaccount.repository.jdbc;

import com.manageaccount.manageaccount.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class AccountJDBCRepository {
    @Autowired
    @Qualifier("demoJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public Long saveAccount(Account account) {
        String query = "INSERT INTO account ( customer_name, email, phone_number) " +
                "VALUES ( :customerName, :email, :phoneNumber )";

        MapSqlParameterSource newQuery = new MapSqlParameterSource();
        newQuery.addValue("customerName", account.getCustomerName());
        newQuery.addValue("email", account.getEmail());
        newQuery.addValue("phoneNumber", account.getPhoneNumber());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query, newQuery, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Account updateAccount(Account account) {
        String query = "Update account set customer_name = :customerName, email = :email, phone_number = :phoneNumber "
                +
                "where account_id = :accountId";

        MapSqlParameterSource newQuery = new MapSqlParameterSource();
        newQuery.addValue("accountId", account.getAccountId());
        newQuery.addValue("customerName", account.getCustomerName());
        newQuery.addValue("email", account.getEmail());
        newQuery.addValue("phoneNumber", account.getPhoneNumber());

        int rowsAffect = jdbcTemplate.update(query, newQuery);
        if (rowsAffect != 0) {
            String selectQuery = "select * from account where account_id = :accountId";
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            parameterSource.addValue("accountId", account.getAccountId());
            return jdbcTemplate.queryForObject(selectQuery, parameterSource, new BeanPropertyRowMapper<>(Account.class));
        } else return null;
    }

    public List<Account> getAllAccounts() {
        String query = "SELECT * FROM account where customer_name = :customerName";
        MapSqlParameterSource newQuery = new MapSqlParameterSource();
        newQuery.addValue("customerName", "haha");

        return jdbcTemplate.queryForList(query, newQuery, Account.class);
    }

    public int deleteAccount(Long accountId) {
        String query = "DELETE FROM account where account_id = :accountId";
        MapSqlParameterSource newQuery = new MapSqlParameterSource();
        newQuery.addValue("accountId", accountId);
        return jdbcTemplate.update(query, newQuery);
    }


}
