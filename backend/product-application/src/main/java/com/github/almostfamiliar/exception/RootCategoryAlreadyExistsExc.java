package com.github.almostfamiliar.exception;

import com.github.almostfamiliar.domain.Category;

public class RootCategoryAlreadyExistsExc extends ApplicationException {
  public RootCategoryAlreadyExistsExc(Category rootNode) {
    super(String.format("Root node with name '%s' already exists!", rootNode.getName()));
  }
}
