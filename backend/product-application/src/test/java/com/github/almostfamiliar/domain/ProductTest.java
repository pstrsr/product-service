package com.github.almostfamiliar.domain;

import com.github.almostfamiliar.exception.ProductHasNoCategoryExc;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

  @Test
  public void should_CreateNewProduct() {
    final Category category = Category.loadExisting(1L, "Winter", new HashSet<>());
    final Product product =
        Product.createNew(
            "Jacket",
            "very warm",
            new Money(BigDecimal.ONE, "EUR"),
            new HashSet<>(Arrays.asList(category)));

    assertEquals(null, product.getId());
  }

  @Test
  public void should_ThrowProductHasNoCategoryError_When_CreatingProductWithoutCategory() {
    assertThrows(
        ProductHasNoCategoryExc.class,
        () ->
            Product.createNew(
                "Jacket", "very warm", new Money(BigDecimal.ONE, "EUR"), new HashSet<>()));
  }
}
