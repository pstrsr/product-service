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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import static feign.FeignException.errorStatus;

@EnableCaching
@EnableScheduling
@Configuration
public class ConverterConfig {

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

  public static class ErrorDecoderImpl implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
      if (response.status() >= 299 || response.status() <= 199) {
        return new CurrencyConversionExc(response.status(), response.reason());
      }
      return errorStatus(methodKey, response);
    }
  }
}
