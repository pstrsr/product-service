package com.github.almostfamiliar.product.currencyconverter;

import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.port.out.ConvertCurrency;
import com.github.almostfamiliar.port.out.ValidateCurrency;
import com.github.almostfamiliar.product.currencyconverter.model.ExchangeRatesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CurrencyAdapter implements ValidateCurrency, ConvertCurrency {

  private final FixerClient fixerClient;

  @Override
  public Money convertToEuro(Money money) {
    final ExchangeRatesDto validCurrencies = fixerClient.getExchangeRates(Money.DEFAULT_CURRENCY);
    final BigDecimal rate = validCurrencies.getRates().get(money.currency());

    return new Money(money.amount().divide(rate, 2, RoundingMode.UP), Money.DEFAULT_CURRENCY);
  }

  @Override
  public Money convertFromEuro(Money money, String currency) {
    final ExchangeRatesDto validCurrencies = fixerClient.getExchangeRates(Money.DEFAULT_CURRENCY);
    final BigDecimal rate = validCurrencies.getRates().get(currency);

    return new Money(money.amount().multiply(rate), currency);
  }

  @Override
  public Set<String> getValidCurrencies() {
    final ExchangeRatesDto validCurrencies = fixerClient.getExchangeRates(Money.DEFAULT_CURRENCY);

    return validCurrencies.getRates().keySet();
  }
}
