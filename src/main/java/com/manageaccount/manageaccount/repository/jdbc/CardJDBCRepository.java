package com.manageaccount.manageaccount.repository.jdbc;

import com.manageaccount.manageaccount.entity.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class CardJDBCRepository {
    @Autowired
    @Qualifier("demoJdbcTemplate")
    NamedParameterJdbcTemplate jdbcTemplate;
    public Long saveCard(Card card ){
        String query = "INSERT INTO card (card_type,expiry_date,status,account_id)" +
                "values(:cardType,:expiryDate,:status,:accountId)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("cardType",card.getCardType());
        params.addValue("expiryDate",card.getExpiryDate());
        params.addValue("status",card.getStatus());
        params.addValue("accountId",card.getAccountId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(query,params,keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int deleteCard(Card card){
        String query = "DELETE FROM card WHERE card_id=:cardId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("cardId",card.getCardId());
        return jdbcTemplate.update(query,params);
    }
}
