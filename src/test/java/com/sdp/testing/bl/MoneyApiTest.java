package com.sdp.testing.bl;

import com.sdp.testing.dao.ExchangeRate;
import com.sdp.testing.dao.ExchangeRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.money.spi.Bootstrap;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MoneyApiTest {

    /**
     This is first test - this is quick dirty but it helps to me to get familiar with some basic conecpts
     Remember - this is not the good unit test!

     Dirty testing -this is NOT normal integration test

     */
    @BeforeClass
    public static void setUp() {
        CustomRateProvider crp = new CustomRateProvider();
        CustomServiceProvider csp = new CustomServiceProvider(crp);
        Bootstrap.init(csp);
    }

    @Test
    public void firstApiTest()  {
        BigDecimal amountDollars = new BigDecimal(20);
        BigDecimal amountPln = new BigDecimal(30);

        Currency currencyUSD = new Currency(amountDollars, CurrencyUnit.USD);
        Currency currencyPLN = new Currency(amountPln, CurrencyUnit.PLN);

        //integration!
        ExchangeRepository exchangeRepositoryMock = mock(ExchangeRepository.class);

        Optional<ExchangeRate> exchangeRate = Optional.of(new ExchangeRate("PLNTOUSD", new BigDecimal(0.26)));
        when(exchangeRepositoryMock.findByKey("PLNTOUSD")).thenReturn(exchangeRate);

        Exchange exchange = new Exchange(exchangeRepositoryMock);
        MonetaryOperation monetaryOperation = new MonetaryOperation(exchange);

        try {
            Currency badAdding = monetaryOperation.add(currencyPLN, currencyUSD);
        } catch (IncompatibleCurrencyUnitException e) {
            String expectedMessage = "Incomaptible currencies! You are trying to add: " + CurrencyUnit.PLN + " " +
                    CurrencyUnit.USD;
            assertEquals(e.getMessage(), expectedMessage);
        }

        Currency currencyAfterAdding = null;
        try {
            currencyAfterAdding = monetaryOperation.add(currencyPLN, currencyPLN);
        } catch (IncompatibleCurrencyUnitException e) {
            fail(); //should not happended
        }
        assertEquals(currencyAfterAdding.getValue(),  new BigDecimal(60));

        Currency monetaryPLNINUSD = monetaryOperation.convert(currencyAfterAdding, CurrencyUnit.USD, 4);

        MathContext mc = new MathContext(4, RoundingMode.HALF_UP);
        assertTrue(monetaryPLNINUSD.getValue()
                .compareTo(new BigDecimal(15.60).round(mc)) == 0);
    }

}
