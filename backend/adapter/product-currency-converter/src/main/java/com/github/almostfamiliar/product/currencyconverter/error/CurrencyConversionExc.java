package com.github.almostfamiliar.product.currencyconverter.error;

public class CurrencyConversionExc extends IllegalStateException {

  public CurrencyConversionExc(int code, String reason) {
    super("Error while converting currencies with code %d. Reason: %s".formatted(code, reason));
  }
}
