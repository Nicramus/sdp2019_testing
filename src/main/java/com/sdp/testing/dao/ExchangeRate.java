package com.sdp.testing.dao;

import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@Entity
public class ExchangeRate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String key;
    private BigDecimal rate;

    public ExchangeRate(){}

    public ExchangeRate(String key, BigDecimal rate) {
        this.key = key;
        this.rate = rate;
    }
}
