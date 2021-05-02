package com.github.almostfamiliar.exception;

public class ProductHasNoCategoryExc extends ApplicationException {
  public ProductHasNoCategoryExc() {
    super("Product has to have at least one category!");
  }
}
