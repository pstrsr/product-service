package com.github.almostfamiliar.exception;

public class CategoryNotEmptyExc extends BusinessException {
  public CategoryNotEmptyExc() {
    super("Category has to be empty to be able to be deleted!");
  }
}
