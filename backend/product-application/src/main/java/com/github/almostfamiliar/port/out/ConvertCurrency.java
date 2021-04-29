package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Money;

public interface ConvertCurrency {
  Money convert(Money money);
}
