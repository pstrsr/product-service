package com.github.almostfamiliar.productweb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyResponse {
  private BigDecimal amount;
  private String currency;
}
