package com.github.almostfamiliar.product.web.mapper;

import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.product.web.dto.response.MoneyResponse;
import org.mapstruct.Mapper;

@Mapper
public interface MoneyWebMapper {
  /** Record mapping bugged in jdk 16. */
  default MoneyResponse toResponse(Money money) {
    final MoneyResponse moneyResponse = new MoneyResponse();
    moneyResponse.setAmount(money.amount());
    moneyResponse.setCurrency(money.currency());
    return moneyResponse;
  }
}
