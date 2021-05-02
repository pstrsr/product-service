package com.github.almostfamiliar.exception;

public class CategoryNotEmptyExc extends ApplicationException {
  public CategoryNotEmptyExc() {
    super(
        "There are still products or subcategories pointing at this category.Category has to be empty to be able to be deleted! ");
  }
}
