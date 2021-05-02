package com.github.almostfamiliar.domain;

import com.github.almostfamiliar.exception.CategoryAlreadyContainsSubcategoryExc;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

  @Test
  public void should_createNewCategory() {
    Category category = Category.createNew("Clothes");

    assertEquals(new HashSet<>(), category.getChildren());
    assertNull(category.getId());
  }

  @Test
  public void should_loadExistingCategory() {
    Category category = Category.loadExisting(1L, "Clothes", new HashSet<>());

    assertEquals(new HashSet<>(), category.getChildren());
    assertNotNull(category.getId());
  }

  @Test
  public void should_AddChildToCategory() {
    Category category = Category.loadExisting(1L, "Clothes", new HashSet<>());

    Category child = Category.loadExisting(2L, "Woman", new HashSet<>());

    category.addChild(child);

    assertEquals(1, category.getChildren().size());
  }

  @Test
  public void should_ThrowSubCategoryAlreadyExists_When_AddingChildWithSameName() {
    Category child = Category.loadExisting(2L, "Woman", new HashSet<>());
    Category category = Category.loadExisting(1L, "Clothes", new HashSet<>(Arrays.asList(child)));
    Category child2 = Category.loadExisting(3L, "Woman", new HashSet<>());

    assertThrows(CategoryAlreadyContainsSubcategoryExc.class, () -> category.addChild(child2));
  }

  @Test
  public void should_RemoveChildCategory() {
    Category child = Category.loadExisting(2L, "Woman", new HashSet<>());
    Category category = Category.loadExisting(1L, "Clothes", new HashSet<>(Arrays.asList(child)));

    category.removeChild(child);

    assertEquals(0, category.getChildren().size());
  }
}
