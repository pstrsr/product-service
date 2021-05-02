package com.github.almostfamiliar.exception;

public class ProductAlreadyExistsExc extends ApplicationException {
  public ProductAlreadyExistsExc(String name) {
    super("Product with the name '%s' already exists!".formatted(name));
  }
}
