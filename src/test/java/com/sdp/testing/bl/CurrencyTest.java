package com.sdp.testing.bl;

import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTest {

    @Test
    public void currencyTestPLN() {
        BigDecimal value = new BigDecimal(3);
        Currency currency = new Currency(value, CurrencyUnit.PLN);
        assertThat(currency.getValue()).isEqualTo(new BigDecimal(3));
        assertThat(currency.getCurrencyUnit()).isEqualTo(CurrencyUnit.PLN);
    }

    @Test
    public void currencyTestUSD() {
        BigDecimal value = new BigDecimal(2);
        Currency currency = new Currency(value, CurrencyUnit.USD);
        assertThat(currency.getValue()).isEqualTo(new BigDecimal(2));
        assertThat(currency.getCurrencyUnit()).isEqualTo(CurrencyUnit.USD);
    }
}
