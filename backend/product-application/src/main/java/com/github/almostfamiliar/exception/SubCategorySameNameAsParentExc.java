package com.github.almostfamiliar.exception;

public class SubCategorySameNameAsParentExc extends ApplicationException {
  public SubCategorySameNameAsParentExc(String name) {
    super(
        String.format(
            "Subcategorie '%s' can not have the same name as any of its ancestors!", name));
  }
}
