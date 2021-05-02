package com.github.almostfamiliar.service;

import com.github.almostfamiliar.port.out.LoadCategory;
import com.github.almostfamiliar.port.out.LoadProduct;
import com.github.almostfamiliar.port.out.WriteCategory;
import com.github.almostfamiliar.port.out.WriteProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ProductServiceTest {
  @Autowired private ProductService sut;
  @MockBean private LoadProduct loadProduct;
  @MockBean private WriteProduct writeProduct;
  @MockBean private LoadCategory loadCategory;
  @MockBean private WriteCategory writeCategory;

  /*  @Test
  void should_createProduct() {
    Category parent = Category.createExisting(1L, "Kitchen", null, new ArrayList<>());
    Money price = new Money(BigDecimal.valueOf(6.00), "EUR");
    Product newProduct = Product.createNew("Pepper", "Tasty spice", price, List.of(parent));

    willDoNothing().given(writeProduct).insert(newProduct);

    sut.createProduct(newProduct);

    verify(writeProduct).insert(newProduct);
  }

  @Test
  void getProduct() {
    Category parent = Category.createExisting(1L, "Kitchen", null, new ArrayList<>());
    Money price = new Money(BigDecimal.valueOf(6.00), "EUR");
    Product newProduct = Product.loadExisting(1L, "Pepper", "Tasty spice", price, List.of(parent));

    willReturn(newProduct).given(loadProduct).byId(1L);

    assertEquals(newProduct, sut.getProduct(1L));
  }

  @Test
  void Should_GetProductWithDifferentCurrency_When_SupplyingCurrencyParam() {
    // TODO
  }

  @Test
  void updateProduct() {
    Category parent = Category.createExisting(1L, "Kitchen", null, new ArrayList<>());
    Money price = new Money(BigDecimal.valueOf(6.00), "EUR");
    Product newProduct = Product.loadExisting(1L, "Pepper", "Tasty spice", price, List.of(parent));

    willDoNothing().given(writeProduct).insert(newProduct);

    sut.createProduct(newProduct);

    verify(writeProduct).insert(newProduct);
  }

  @Test
  void deleteProduct() {
    Category parent = Category.createExisting(1L, "Kitchen", null, new ArrayList<>());
    Money price = new Money(BigDecimal.valueOf(6.00), "EUR");
    Product newProduct = Product.loadExisting(1L, "Pepper", "Tasty spice", price, List.of(parent));

    willDoNothing().given(writeProduct).insert(newProduct);

    sut.createProduct(newProduct);

    verify(writeProduct).insert(newProduct);
  }*/
}
