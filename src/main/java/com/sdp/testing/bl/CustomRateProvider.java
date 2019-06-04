package com.sdp.testing.bl;

import org.javamoney.moneta.convert.ExchangeRateBuilder;
import org.javamoney.moneta.spi.AbstractRateProvider;
import org.javamoney.moneta.spi.DefaultNumberValue;

import javax.money.Monetary;
import javax.money.convert.*;
import java.util.ArrayList;
import java.util.List;

public class CustomRateProvider extends AbstractRateProvider {

    private List<ExchangeRate> exchangeRates = new ArrayList<>();

    public static final ProviderContext CONTEXT =
            ProviderContextBuilder.of("LOCAL_RATE_PROVIDER", RateType.DEFERRED)
                    .set("providerDescription", "This is local rate provider")
                    .set("days", 1).build();

    /**
     * Constructor
     */
    public CustomRateProvider() {
        super(CONTEXT);

        ExchangeRateBuilder builder;

        builder = new ExchangeRateBuilder(ConversionContextBuilder.create(CONTEXT, RateType.DEFERRED).build());
        builder.setTerm(Monetary.getCurrency("PLN"));
        builder.setBase(Monetary.getCurrency("USD"));
        builder.setFactor(DefaultNumberValue.of(0.26));
        exchangeRates.add(builder.build());

        builder = new ExchangeRateBuilder(ConversionContextBuilder.create(CONTEXT, RateType.DEFERRED).build());
        builder.setBase(Monetary.getCurrency("PLN"));
        builder.setTerm(Monetary.getCurrency("USD")); //target currency
        builder.setFactor(DefaultNumberValue.of(3.83));

        exchangeRates.add(builder.build());
    }

    @Override
    public ExchangeRate getExchangeRate(ConversionQuery conversionQuery) {
        return exchangeRates.stream().filter(x -> x.getBaseCurrency()
                .equals(conversionQuery.getBaseCurrency())).findFirst().get();
    }
}
