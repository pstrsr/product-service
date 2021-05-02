package com.github.almostfamiliar.product.currencyconverter.config;

import com.github.almostfamiliar.product.currencyconverter.error.CurrencyConversionExc;
import com.github.almostfamiliar.product.currencyconverter.rest.FixerFeignClient;
import feign.Feign;
import feign.Logger;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static com.github.almostfamiliar.product.currencyconverter.config.CacheConfig.CURRENCY_CACHE;
import static feign.FeignException.errorStatus;

@Slf4j
@EnableCaching
@EnableScheduling
@Configuration
public class ConverterConfig {
  public static final int MINUTES_10 = 10 * 60 * 1000;

  @Value("${fixer.url:http://data.fixer.io/api/}")
  private String fixerUrl;

  @Bean
  public FixerFeignClient requestInterceptor() {
    return Feign.builder()
        .client(new OkHttpClient())
        .encoder(new GsonEncoder())
        .decoder(new GsonDecoder())
        .logger(new Slf4jLogger(FixerFeignClient.class))
        .errorDecoder(new ErrorDecoderImpl())
        .logLevel(Logger.Level.FULL)
        .target(FixerFeignClient.class, fixerUrl);
  }

  @CacheEvict(allEntries = true, value = CURRENCY_CACHE)
  @Scheduled(fixedDelay = MINUTES_10, initialDelay = 10)
  public void cacheTTL() {
    log.info("Flushing currency exchange rate cache");
  }

  public static class ErrorDecoderImpl implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
      if (response.status() >= 299 || response.status() <= 199) {
        throw new CurrencyConversionExc(response.status(), response.reason());
      }
      return errorStatus(methodKey, response);
    }
  }
}
