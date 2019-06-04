package com.sdp.testing.bl;

import java.math.BigDecimal;

public class Currency {
    private BigDecimal value;
    private CurrencyUnit currencyUnit;

    public Currency() { }

    public Currency(BigDecimal value, CurrencyUnit currencyUnit) {
        this.value = value;
        this.currencyUnit = currencyUnit;
    }

    public CurrencyUnit getCurrencyUnit() {
        return currencyUnit;
    }

    public BigDecimal getValue() {
        return value;
    }
}
