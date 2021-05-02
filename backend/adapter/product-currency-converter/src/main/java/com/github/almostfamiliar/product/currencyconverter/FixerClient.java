package com.github.almostfamiliar.product.currencyconverter;

import com.github.almostfamiliar.product.currencyconverter.config.CacheConfig;
import com.github.almostfamiliar.product.currencyconverter.model.ExchangeRatesDto;
import com.github.almostfamiliar.product.currencyconverter.rest.FixerFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FixerClient {
  @Value("${fixer.api.key}")
  private String apiKey;

  private final FixerFeignClient fixerFeignClient;

  @Cacheable(CacheConfig.CURRENCY_CACHE)
  public ExchangeRatesDto getExchangeRates(String currency) {
    return fixerFeignClient.getExchangeRates(apiKey, currency);
  }
}
