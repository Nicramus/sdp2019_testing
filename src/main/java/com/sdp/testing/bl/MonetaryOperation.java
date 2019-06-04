package com.sdp.testing.bl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * This class have got all necessary monetary operations
 * Like converting monetary units, adding and it may have other in the future
 */
@Service
public class MonetaryOperation {

    private Exchange exchange;

    /**
     * MonetaryOperation requires exchange object which is used to currency conversion
     * @param exchange exchange object
     */
    public MonetaryOperation(Exchange exchange) {
        this.exchange = exchange;
    }

    /**
     *
     * @param currencyAmount
     * @param currencyUnit
     * @return
     */
    public Currency convert(Currency currencyAmount, CurrencyUnit currencyUnit, int precision) {
        BigDecimal exchangeRate = exchange.getExchangeRateFromDb(currencyAmount.getCurrencyUnit(), currencyUnit);
        BigDecimal convertedAmount = currencyAmount.getValue().multiply(exchangeRate);
        return new Currency(convertedAmount.round(new MathContext(precision, RoundingMode.HALF_UP)), currencyUnit);
    }

    /**
     *
     * @param amount1
     * @param amount2
     * @return
     * @throws IncompatibleCurrencyUnitException
     */
    public Currency add(Currency amount1, Currency amount2)
            throws IncompatibleCurrencyUnitException {

        Currency monetaryAmount;
        if (amount1.getCurrencyUnit().equals(amount2.getCurrencyUnit())) {
            BigDecimal amount = amount1.getValue().add(amount2.getValue());
            monetaryAmount = new Currency(amount, amount1.getCurrencyUnit());
        } else {
            String message = "Incomaptible currencies! You are trying to add: "
                    + amount1.getCurrencyUnit() + " " + amount2.getCurrencyUnit();
            throw new IncompatibleCurrencyUnitException(message);
        }
        return monetaryAmount;
    }

}
