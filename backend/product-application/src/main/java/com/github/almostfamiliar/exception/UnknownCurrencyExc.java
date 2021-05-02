package com.github.almostfamiliar.exception;

public class UnknownCurrencyExc extends ApplicationException {
  public UnknownCurrencyExc(String currency) {
    super(String.format("Currency '%s' is unknown!", currency));
  }
}
