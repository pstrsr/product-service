package com.github.almostfamiliar.port.in;

import com.github.almostfamiliar.domain.Category;

import java.math.BigInteger;

public interface CategoryCRUD {
  void createCategory(Category category);

  Category getCategory(BigInteger id, String currency);

  void updateCategory(Category category);

  void deleteCategory(BigInteger id);
}
