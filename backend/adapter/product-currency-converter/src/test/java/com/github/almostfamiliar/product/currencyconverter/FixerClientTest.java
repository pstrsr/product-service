package com.github.almostfamiliar.product.currencyconverter;

import com.github.almostfamiliar.product.currencyconverter.model.ExchangeRatesDto;
import com.github.almostfamiliar.product.currencyconverter.rest.FixerFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class FixerClientTest {
  @Autowired FixerClient sut;
  @MockBean FixerFeignClient fixerClient;

  @Test
  public void testCache() {
    ExchangeRatesDto exchangeRatesDto = new ExchangeRatesDto();
    given(fixerClient.getExchangeRates(anyString(), anyString())).willReturn(exchangeRatesDto);

    sut.getExchangeRates("USD");
    sut.getExchangeRates("USD");

    verify(fixerClient, times(1)).getExchangeRates(anyString(), anyString());
  }
}
