package com.sdp.testing.bl;

import com.sdp.testing.dao.ExchangeRate;
import com.sdp.testing.dao.ExchangeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test shows how we can use mockito
 * https://site.mockito.org/
 */

@RunWith(MockitoJUnitRunner.class)
public class ExchangeTest {

    @Mock
    private ExchangeRepository exchangeRepository;

    @Test
    public void testGetExchangeRateFromDbUSDTOPLN() {
        //mocked dependency
        Optional<ExchangeRate> exchangeRate = Optional.of(new ExchangeRate("USDTOPLN", new BigDecimal(3)));
        Mockito.when(exchangeRepository.findByKey("USDTOPLN")).thenReturn(exchangeRate);
        Exchange exchange = new Exchange(exchangeRepository);
        BigDecimal exchangeRateFromDb = exchange.getExchangeRateFromDb(CurrencyUnit.USD, CurrencyUnit.PLN);
        assertThat(exchangeRateFromDb).isEqualTo(new BigDecimal(3));
    }

    @Test
    public void testGetExchangeRateFromDbPLNTOUSD() {
        //mocked dependency
        Optional<ExchangeRate> exchangeRate = Optional.of(new ExchangeRate("PLNTOUSD", new BigDecimal(0.3)));
        Mockito.when(exchangeRepository.findByKey("PLNTOUSD")).thenReturn(exchangeRate);
        Exchange exchange = new Exchange(exchangeRepository);
        BigDecimal exchangeRateFromDb = exchange.getExchangeRateFromDb(CurrencyUnit.PLN, CurrencyUnit.USD);
        assertThat(exchangeRateFromDb).isEqualTo(new BigDecimal(0.3));
    }

    @Test(expected = NonExistendCurrencyUnitException.class)
    public void nonExistendCurrencyUnitException() {
        //mocked dependency
        Exchange exchange = new Exchange(exchangeRepository);
        exchange.getExchangeRateFromDb(CurrencyUnit.PLN, CurrencyUnit.CHF);
    }
}
