package com.github.almostfamiliar.product.currencyconverter.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static java.util.Arrays.asList;

@Slf4j
@Component
public class CacheConfig implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

  public static final String CURRENCY_CACHE = "currency";

  @Override
  public void customize(ConcurrentMapCacheManager cacheManager) {
    cacheManager.setCacheNames(asList(CURRENCY_CACHE));
  }


}
