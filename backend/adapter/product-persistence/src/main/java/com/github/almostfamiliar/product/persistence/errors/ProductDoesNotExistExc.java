package com.github.almostfamiliar.product.persistence.errors;

import com.github.almostfamiliar.exception.NotFoundException;

public class ProductDoesNotExistExc extends NotFoundException {
  public ProductDoesNotExistExc(Long id) {
    super("Product with ID '%s' does not exist".formatted(id));
  }
}
