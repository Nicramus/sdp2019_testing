package com.sdp.testing.config;

import com.sdp.testing.bl.CurrencyUnit;
import org.springframework.core.convert.converter.Converter;

public class StringToEnumConverter implements Converter<String, CurrencyUnit> {

    @Override
    public CurrencyUnit convert(String from) {
        return CurrencyUnit.valueOf(from);
    }

}

