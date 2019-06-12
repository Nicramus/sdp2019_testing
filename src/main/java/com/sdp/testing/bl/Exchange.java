package com.sdp.testing.bl;

import com.sdp.testing.dao.ExchangeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.money.Monetary;
import javax.money.convert.*;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Business logic - class which is used for making exchange
 */
@Component
public class Exchange {

    private ExchangeRepository exchangeRepository;

    @Autowired
    public Exchange(ExchangeRepository exchangeRepository) {
        this.exchangeRepository = exchangeRepository;
    }

    @PostConstruct
    public void init() {
        com.sdp.testing.dao.ExchangeRate er1 = new com.sdp.testing.dao.ExchangeRate("USDTOPLN", new BigDecimal(3.84));
        com.sdp.testing.dao.ExchangeRate er2 = new com.sdp.testing.dao.ExchangeRate("PLNTOUSD", new BigDecimal(0.26));
        exchangeRepository.save(er1);
        exchangeRepository.save(er2);
    }

    /**
     * Returns exchange rate from db
     * @param cu1 input CurrencyUnit
     * @param cu2 target CurrencyUnit
     * @return
     */
    public BigDecimal getExchangeRateFromDb(CurrencyUnit cu1, CurrencyUnit cu2) {
        String exchangeRateKey = cu1.toString() + "TO" + cu2.toString();
        Optional<com.sdp.testing.dao.ExchangeRate> exchangeRateOptional = exchangeRepository.findByKey(exchangeRateKey);
        com.sdp.testing.dao.ExchangeRate exchangeRate = null;

        if(exchangeRateOptional.isPresent()) {
            exchangeRate = exchangeRateOptional.get();
        } else {
            throw new NonExistendCurrencyUnitException("Exchange rate not found");
        }
        return exchangeRate.getRate();
    }

    /**
     * Returns exchange rate by java money rate provider
     *
     * @param cu1 input CurrencyUnit
     * @param cu2 target CurrencyUnit
     * @return BigDecimal exchange rate
     */
    public BigDecimal getExchangeRateByRateProvider(CurrencyUnit cu1, CurrencyUnit cu2) {
        ConversionQuery cq = ConversionQueryBuilder.of().setTermCurrency(cu2.toString()).build();
        CurrencyConversion conversion = MonetaryConversions.getConversion(cq);

        javax.money.CurrencyUnit currencyUnit = javax.money.Monetary.getCurrency(cu1.toString());
        javax.money.MonetaryAmount amount = Monetary.getDefaultAmountFactory()
                .setCurrency(currencyUnit).setNumber(1).create();

        javax.money.convert.ExchangeRate exchangeRate = conversion.getExchangeRate(amount); //get exchange rate USD to EUR
        //TODO https://dzone.com/articles/looking-java-9-money-and
        //by default rate from ECB-HIST (exchange default provider)
        return new BigDecimal(exchangeRate.getFactor().doubleValue());
    }
}
