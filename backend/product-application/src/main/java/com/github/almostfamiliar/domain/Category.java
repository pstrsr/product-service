package com.github.almostfamiliar.domain;

import com.github.almostfamiliar.exception.CategoryAlreadyContainsSubcategoryExc;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class Category {
  @Getter private final Long id;
  @Getter private String name;
  @Getter private final Set<Category> children;

  private Category(Long id, String name, Set<Category> children) {
    this.id = id;
    this.name = name;
    this.children = children;
  }

  public static Category createNew(String name) {
    return new Category(null, name, new HashSet<>());
  }

  public static Category loadExisting(Long id, String name, Set<Category> children) {
    return new Category(id, name, children);
  }

  public void addChild(Category newChild) {
    checkChildrenAlreadyContainName(newChild);

    this.children.add(newChild);
  }

  public void changeName(String name) {
    this.name = name;
  }

  public void removeChild(Category oldChild) {
    final Optional<Category> oldNode =
        this.children.stream().filter(child -> child.getId().equals(oldChild.getId())).findAny();

    oldNode.ifPresent(this.children::remove);
  }

  private void checkChildrenAlreadyContainName(Category newChild) {
    boolean alreadyContainsName =
        this.children.stream()
            .anyMatch(child -> child.getName().equalsIgnoreCase(newChild.getName()));
    if (alreadyContainsName) {
      throw new CategoryAlreadyContainsSubcategoryExc(this, newChild);
    }
  }
}
