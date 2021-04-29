package com.github.almostfamiliar.domain;

import lombok.Getter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Category {
  @Getter private BigInteger id;
  @Getter private String name;
  @Getter private Category parent;
  @Getter private List<Category> childs;

  private Category(BigInteger id, String name, Category parent, List<Category> childs) {
    this.id = id;
    this.name = name;
    this.parent = parent;
    this.childs = childs;
  }

  public static Category createNew(String name, Category parent) {
    return new Category(null, name, parent, new ArrayList<>());
  }

  public static Category load(BigInteger id, String name, Category parent, List<Category> childs) {
    return new Category(id, name, parent, childs);
  }
}
