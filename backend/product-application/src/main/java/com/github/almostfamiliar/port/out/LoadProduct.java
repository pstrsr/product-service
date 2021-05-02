package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Product;

import java.util.Set;

public interface LoadProduct {
  Product byId(Long id);

  Set<Product> byCategory(Long categoryId);

  boolean existsByName(String name);

  boolean existsOtherByName(Long id, String name);
}
