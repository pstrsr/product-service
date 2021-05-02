package com.github.almostfamiliar.domain;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Money (BigDecimal amount, String currency){
    public static String DEFAULT_CURRENCY = "EUR";

    public Money(@NotNull BigDecimal amount,String currency) {
        this.amount = amount;
        this.currency = currency == null ? DEFAULT_CURRENCY : currency;
        if(amount == null){
            throw new IllegalStateException("Money amount can not be null!");
        }
    }
}
