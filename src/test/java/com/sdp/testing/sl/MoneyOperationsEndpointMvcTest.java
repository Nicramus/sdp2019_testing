package com.sdp.testing.sl;


import com.sdp.testing.bl.Currency;
import com.sdp.testing.bl.CurrencyUnit;
import com.sdp.testing.bl.Exchange;
import com.sdp.testing.bl.MonetaryOperation;
import com.sdp.testing.dao.ExchangeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Only MockMvc test - server not starting
 */
@RunWith(SpringRunner.class)
@WebMvcTest(MoneyOperationsEndpoint.class)
public class MoneyOperationsEndpointMvcTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Exchange exchange;

    @MockBean
    private ExchangeRepository exchangeRepository;

    @MockBean
    private MonetaryOperation monetaryOperation;

    private String MONEY_OPERATIONS_ENDPOINT_URL = "/monetary_operations/v1";

    @Test
    public void testConversion() throws Exception {
        // given
        given(exchange.getExchangeRateFromDb(CurrencyUnit.USD, CurrencyUnit.PLN)).willReturn(new BigDecimal(4));

        Currency currencyToConvert = new Currency(new BigDecimal(2), CurrencyUnit.USD);
        Currency targetCurrency = new Currency(new BigDecimal(8), CurrencyUnit.PLN);

        given(monetaryOperation.convert(any(Currency.class), any(CurrencyUnit.class), anyInt())).willReturn(targetCurrency);

        String convert = MONEY_OPERATIONS_ENDPOINT_URL + "/convert";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(convert)
                // Add query parameter
                .queryParam("inputValue", "2")
                .queryParam("inputUnit", "PLN")
                .queryParam("targetUnit", "USD");

        // when
        MockHttpServletResponse response = mvc.perform(
                get(builder.toUriString())
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String body = "{\"value\":8,\"currencyUnit\":\"PLN\"}";
        assertThat(response.getContentAsString()).contains(body);
    }
}