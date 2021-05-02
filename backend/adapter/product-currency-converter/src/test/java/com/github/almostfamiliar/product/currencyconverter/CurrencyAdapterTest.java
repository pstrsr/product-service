package com.github.almostfamiliar.product.currencyconverter;

import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.product.currencyconverter.model.ExchangeRatesDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class CurrencyAdapterTest {
  @Autowired CurrencyAdapter sut;
  @MockBean FixerClient fixerClient;

  private ExchangeRatesDto exchangeRatesDto;

  @BeforeEach
  public void mockExchangeRates() throws IOException {
    final File file = ResourceUtils.getFile("classpath:exchange_rates.json");
    String exchangeRatesAsString = Files.readString(file.toPath());
    exchangeRatesDto = new Gson().fromJson(exchangeRatesAsString, ExchangeRatesDto.class);

    given(fixerClient.getExchangeRates(Money.DEFAULT_CURRENCY)).willReturn(exchangeRatesDto);
  }

  @Test
  public void should_ConvertToEuro() {
    Money usd = new Money(BigDecimal.valueOf(1.50), "USD");
    Money eur = new Money(BigDecimal.valueOf(1.25), "EUR");

    assertEquals(eur, sut.convertToEuro(usd));
  }

  @Test
  public void should_ConvertFromEuro() {
    Money usd = new Money(BigDecimal.valueOf(1.8029925), "USD");
    Money eur = new Money(BigDecimal.valueOf(1.50), "EUR");

    assertEquals(usd, sut.convertFromEuro(eur, "USD"));
  }

  @Test
  public void should_ValidCurrencies() {
    assertEquals(exchangeRatesDto.getRates().keySet(), sut.getValidCurrencies());
  }
}
