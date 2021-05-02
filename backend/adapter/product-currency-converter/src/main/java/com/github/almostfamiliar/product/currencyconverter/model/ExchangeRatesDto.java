package com.github.almostfamiliar.product.currencyconverter.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRatesDto {
  private boolean success;
  private int timestamp;
  private String base;
  private String date;
  private Map<String, BigDecimal> rates;
}
