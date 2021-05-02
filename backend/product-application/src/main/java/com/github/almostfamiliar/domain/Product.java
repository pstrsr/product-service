package com.github.almostfamiliar.domain;

import com.github.almostfamiliar.exception.ProductHasNoCategoryExc;
import lombok.Getter;

import java.util.Set;

public class Product {
  @Getter private final Long id;
  @Getter private final String name;
  @Getter private final String description;
  @Getter private Money price;
  @Getter private final Set<Category> categories;

  private Product(Long id, String name, String description, Money price, Set<Category> categories) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.categories = categories;
    if (categories == null || categories.isEmpty()) {
      throw new ProductHasNoCategoryExc();
    }
  }

  public static Product createNew(
      String name, String description, Money price, Set<Category> categories) {
    return new Product(null, name, description, price, categories);
  }

  public static Product loadExisting(
      Long id, String name, String description, Money price, Set<Category> categories) {
    return new Product(id, name, description, price, categories);
  }

  public void convertCurrency(Money price) {
    this.price = price;
  }
}
