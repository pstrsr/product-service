package com.github.almostfamiliar.product.persistence.errors;

import com.github.almostfamiliar.exception.NotFoundException;

public class CategoryDoesNotExistExc extends NotFoundException {
  public CategoryDoesNotExistExc(Long id) {
    super(String.format("Category with id '%s' does not exist!", id));
  }
}
