package com.sdp.testing.sl;

import com.sdp.testing.bl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Monetary Operations Endpoint
 */
@RestController
@RequestMapping("/monetary_operations/v1")
public class MoneyOperationsEndpoint {

    @Autowired
    private MonetaryOperation monetaryOperation;

    /**
     * Return converted currency
     * Exception when unknown currency unit is used
     */
    @GetMapping("/convert")
    public ResponseEntity<Currency> convertCurrency(@RequestParam BigDecimal inputValue,
                                                    @RequestParam CurrencyUnit inputUnit,
                                                    @RequestParam CurrencyUnit targetUnit) {
        Currency currency = new Currency(inputValue, inputUnit);
        Currency convertedCurrency = monetaryOperation.convert(currency, targetUnit, 4);
        return ResponseEntity.ok(convertedCurrency);
    }

    /**
     * Sum two currency values.
     */
    @GetMapping("/sum")
    public ResponseEntity<Currency> sumCurrencies(@RequestParam BigDecimal firstValue,
                              @RequestParam BigDecimal secondValue,
                              @RequestParam CurrencyUnit targetUnit) {

        Currency inputCurrency = new Currency(firstValue, targetUnit);
        Currency targetCurrency = new Currency(secondValue, targetUnit);
        Currency sum = null;
        try {
            sum = monetaryOperation.add(inputCurrency, targetCurrency);
        } catch (IncompatibleCurrencyUnitException e) {
            //should not happen
            e.printStackTrace();
        }
        return ResponseEntity.ok(sum);
    }
}
