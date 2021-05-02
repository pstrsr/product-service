package com.github.almostfamiliar.exception;

import com.github.almostfamiliar.domain.Category;

public class CategoryAlreadyContainsSubcategoryExc extends ApplicationException {
  public CategoryAlreadyContainsSubcategoryExc(Category parentNode, Category savedNode) {
    super(
        String.format(
            "Parent node '%s' already has a child named '%s'. Please choose another name.",
            parentNode.getName(), savedNode.getName()));
  }
}
