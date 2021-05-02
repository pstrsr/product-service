package com.github.almostfamiliar.product.currencyconverter.rest;

import com.github.almostfamiliar.product.currencyconverter.model.ExchangeRatesDto;
import feign.Param;
import feign.RequestLine;

public interface FixerFeignClient {
  @RequestLine("GET /latest?access_key={key}&base={base}")
  ExchangeRatesDto getExchangeRates(
      @Param(value = "key") String key, @Param(value = "base") String base);
}
