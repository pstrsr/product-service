package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Money;

public interface ConvertCurrency {
  Money convertToEuro(Money money);

  Money convertFromEuro(Money money, String currency);
}
