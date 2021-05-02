package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.exception.ProductAlreadyExistsExc;
import com.github.almostfamiliar.port.command.CreateProductCmd;
import com.github.almostfamiliar.port.command.UpdateProductCmd;
import com.github.almostfamiliar.port.out.*;
import com.github.almostfamiliar.validators.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ProductServiceTest {
  @Autowired private ProductService sut;
  @MockBean private LoadProduct loadProduct;
  @MockBean private WriteProduct writeProduct;
  @MockBean private LoadCategory loadCategory;
  @MockBean private WriteCategory writeCategory;
  @MockBean private ConvertCurrency convertCurrency;
  @MockBean private Currency.CurrencyValidator currencyValidator;
  @MockBean private ValidateCurrency validateCurrency;

  @Test
  void should_CreateProduct() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;

    final Money price = new Money(productPrice, currency);
    final Product newProduct =
        Product.loadExisting(2L, productName, productDescription, price, parent);

    final CreateProductCmd createProductCmd =
        new CreateProductCmd(
            productName, productDescription, productPrice, currency, List.of(categoryId));

    given(loadCategory.byIds(List.of(categoryId)))
        .willReturn(new HashSet<>(Collections.singletonList(parent)));
    willDoNothing().given(writeProduct).upsert(any());
    given(loadProduct.existsByName(productName)).willReturn(false);

    sut.createProduct(createProductCmd);

    verify(writeProduct, times(1)).upsert(any(Product.class));
  }

  @Test
  void should_ThrowProductAreadyExistsErr_When_CreatingProductWhereNameAlreadyExists() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;

    final Money price = new Money(productPrice, currency);
    final Product newProduct =
        Product.loadExisting(2L, productName, productDescription, price, parent);

    final CreateProductCmd createProductCmd =
        new CreateProductCmd(
            productName, productDescription, productPrice, currency, List.of(categoryId));

    given(loadCategory.byIds(List.of(categoryId)))
        .willReturn(new HashSet<>(Collections.singletonList(parent)));
    willDoNothing().given(writeProduct).upsert(newProduct);
    given(loadProduct.existsByName(productName)).willReturn(true);

    assertThrows(ProductAlreadyExistsExc.class, () -> sut.createProduct(createProductCmd));
  }

  @Test
  void should_GetProductByID() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;
    final long productId = 2L;

    final Money price = new Money(productPrice, currency);
    final Product product =
        Product.loadExisting(productId, productName, productDescription, price, parent);

    given(loadProduct.byId(productId)).willReturn(product);

    assertEquals(product, sut.getProduct(productId));
  }

  @Test
  void should_GetProductByIDWithCurrencyConverted_When_RequestingUSD() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String eur = "EUR";
    final long productId = 2L;

    final String usd = "USD";
    final Money priceInEuro = new Money(productPrice, eur);
    final Money priceInUSD = new Money(productPrice, usd);
    final Product productInEuro =
        Product.loadExisting(productId, productName, productDescription, priceInEuro, parent);

    final Product productInUSD =
        Product.loadExisting(productId, productName, productDescription, priceInUSD, parent);

    given(loadProduct.byId(productId)).willReturn(productInEuro);
    given(convertCurrency.convertFromEuro(priceInEuro, usd)).willReturn(priceInUSD);
    given(validateCurrency.getValidCurrencies())
        .willReturn(new HashSet<>(Arrays.asList("EUR", "USD", "CHF")));

    assertEquals(productInUSD, sut.getProduct(productId, usd));
  }

  @Test
  void should_ThrowValidationError_When_RequestingUnknownCurrency() {
    final long productId = 2L;
    final String unknownCurrency = "BITCOIN";

    given(validateCurrency.getValidCurrencies())
        .willReturn(new HashSet<>(Arrays.asList("EUR", "USD", "CHF")));

    assertThrows(
        ConstraintViolationException.class, () -> sut.getProduct(productId, unknownCurrency));
  }

  @Test
  void should_UpdateProduct() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;

    final long productId = 2L;

    final UpdateProductCmd updateProductCmd =
        new UpdateProductCmd(
            productId,
            productName,
            productDescription,
            productPrice,
            currency,
            List.of(categoryId));

    given(loadCategory.byIds(List.of(categoryId)))
        .willReturn(new HashSet<>(Collections.singletonList(parent)));
    given(loadProduct.existsOtherByName(productId, productName)).willReturn(false);

    sut.updateProduct(updateProductCmd);

    verify(writeProduct, times(1)).upsert(any(Product.class));
  }

  @Test
  void should_ThrowNameAlreadyExistsError_When_UpdateProductWithDuplicateName() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;

    final long productId = 2L;

    final UpdateProductCmd updateProduct =
        new UpdateProductCmd(
            productId,
            productName,
            productDescription,
            productPrice,
            currency,
            List.of(categoryId));

    given(loadCategory.byIds(List.of(categoryId)))
        .willReturn(new HashSet<>(Collections.singletonList(parent)));
    given(loadProduct.existsOtherByName(productId, productName)).willReturn(true);

    assertThrows(ProductAlreadyExistsExc.class, () -> sut.updateProduct(updateProduct));
  }

  @Test
  public void should_GetProductsByCategory() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;
    final long productId = 2L;

    final Money price = new Money(productPrice, currency);
    final Product product1 =
        Product.loadExisting(productId, productName, productDescription, price, parent);

    final BigDecimal product2Price = BigDecimal.valueOf(3.33);
    final String product2Name = "Salt";
    final String product2Description = "Peppers buddy";
    final String currency2 = null;
    final long product2Id = 3L;

    final Money price2 = new Money(product2Price, currency2);
    final Product product2 =
        Product.loadExisting(product2Id, product2Name, product2Description, price2, parent);

    final HashSet<Product> productsByCategory = new HashSet<>(Arrays.asList(product1, product2));
    given(loadProduct.byCategory(categoryId)).willReturn(productsByCategory);

    assertEquals(productsByCategory, sut.getProductsByCategory(categoryId));
  }

  @Test
  public void should_ConvertToUSD_When_QueryingProductsByCategoryWithCurrency() {
    final long categoryId = 1L;
    final Category parent = Category.loadExisting(categoryId, "Kitchen", new HashSet<>());

    final BigDecimal productPrice = BigDecimal.valueOf(6.00);
    final String productName = "Pepper";
    final String productDescription = "Tasty spice";
    final String currency = null;
    final long productId = 2L;

    final Money price = new Money(productPrice, currency);
    final Product product1 =
        Product.loadExisting(productId, productName, productDescription, price, parent);

    final BigDecimal product2Price = BigDecimal.valueOf(3.33);
    final String product2Name = "Salt";
    final String product2Description = "Peppers buddy";
    final String currency2 = null;
    final long product2Id = 3L;

    final Money price2 = new Money(product2Price, currency2);
    final Product product2 =
        Product.loadExisting(product2Id, product2Name, product2Description, price2, parent);
    final String targetCurrency = "USD";

    final Money priceInUSD = new Money(BigDecimal.ONE, targetCurrency);
    final HashSet<Product> productsByCategory = new HashSet<>(Arrays.asList(product1, product2));

    given(loadProduct.byCategory(categoryId)).willReturn(productsByCategory);
    given(convertCurrency.convertFromEuro(any(), eq(targetCurrency))).willReturn(priceInUSD);
    given(validateCurrency.getValidCurrencies())
        .willReturn(new HashSet<>(Arrays.asList("EUR", "USD", "CHF")));

    long amountOfCurrenciesNotEqualTargetCurrencies =
        sut.getProductsByCategory(categoryId, targetCurrency).stream()
            .map(Product::getPrice)
            .map(Money::currency)
            .filter(curr -> !curr.equalsIgnoreCase(targetCurrency))
            .count();
    assertEquals(0, amountOfCurrenciesNotEqualTargetCurrencies);
  }

  @Test
  void deleteProduct() {
    final long productId = 2L;

    sut.deleteProduct(productId);

    verify(writeProduct).delete(productId);
  }
}
