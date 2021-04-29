package com.github.almostfamiliar.domain;

import lombok.Getter;

import java.math.BigInteger;
import java.util.List;

public class Product {
  @Getter private BigInteger id;
  @Getter private String name;
  @Getter private String description;
  @Getter private Money price;
  @Getter private List<Category> categories;

  private Product(
      BigInteger id, String name, String description, Money price, List<Category> categories) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.categories = categories;
  }

  public static Product createNew(
      String name, String description, Money price, List<Category> categories) {
    return new Product(null, name, description, price, categories);
  }

  public static Product load(
      BigInteger id, String name, String description, Money price, List<Category> categories) {
    return new Product(id, name, description, price, categories);
  }

  public void convertCurrency(Money price) {
    this.price = price;
  }
}
