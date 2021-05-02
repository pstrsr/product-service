package com.github.almostfamiliar.exception;

import com.github.almostfamiliar.domain.Category;

public class CategoryAlreadyContainsSameSubcategoryExc extends ApplicationException {
  public CategoryAlreadyContainsSameSubcategoryExc(Category parentNode, Category savedNode) {
    super(
        String.format(
            "Parent node '%s' already has a child named '%s'. Please choose another name.",
            parentNode.getName(), savedNode.getName()));
  }
}
