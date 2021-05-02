package com.github.almostfamiliar.product.web.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyResponse {
  private BigDecimal amount;
  private String currency;
}
