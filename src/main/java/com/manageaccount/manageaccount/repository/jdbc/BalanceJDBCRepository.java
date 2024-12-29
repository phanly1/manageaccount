package com.manageaccount.manageaccount.repository.jdbc;

import com.manageaccount.manageaccount.entity.Account;
import com.manageaccount.manageaccount.entity.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BalanceJDBCRepository {
    @Autowired
    @Qualifier("demoJdbcTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    public int deleteBalanceByAccountId(Long accountId) {
        String query = "DELETE FROM balance WHERE account_id=:accountId";
        Map<String, Object> newQuery = new HashMap<String, Object>();
        newQuery.put("accountId", accountId);
        return jdbcTemplate.update(query, newQuery);
    }

    public int updatebalance(Balance balance) {
        String query = "UPDATE balance SET available_balance=:availableBalance WHERE account_id=:accountId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("availableBalance", balance.getAvailableBalance());
        params.addValue("accountId", balance.getAccountId());

        return jdbcTemplate.update(query, params);
//        int rowAffected = jdbcTemplate.update(query, params);
//        if(rowAffected != 0){
//            String selectquery = "SELECT * FROM balance WHERE account_id=:accountId";
//            MapSqlParameterSource param = new MapSqlParameterSource();
//            param.addValue("accountId", balance.getAccountId());
//
//            return jdbcTemplate.queryForObject(selectquery, param, new BeanPropertyRowMapper<>(Balance.class));
//        } else return null;
    }
}
