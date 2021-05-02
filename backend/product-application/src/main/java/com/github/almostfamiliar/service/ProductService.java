package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.exception.ProductAlreadyExistsExc;
import com.github.almostfamiliar.port.command.CreateProductCmd;
import com.github.almostfamiliar.port.command.UpdateProductCmd;
import com.github.almostfamiliar.port.in.ProductCRUD;
import com.github.almostfamiliar.port.out.ConvertCurrency;
import com.github.almostfamiliar.port.out.LoadCategory;
import com.github.almostfamiliar.port.out.LoadProduct;
import com.github.almostfamiliar.port.out.WriteProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Set;

@Slf4j
@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class ProductService implements ProductCRUD {
  private final LoadProduct loadProduct;
  private final LoadCategory loadCategory;
  private final WriteProduct writeProduct;
  private final ConvertCurrency convertCurrency;

  @Override
  public void createProduct(@Valid CreateProductCmd product) {
    final Set<Category> categories = loadCategory.byIds(product.categories());
    final Money price = createMoney(product.price(), product.currency());
    final Product newProduct =
        Product.createNew(product.name(), product.description(), price, categories);

    if (loadProduct.existsByName(newProduct.getName())) {
      throw new ProductAlreadyExistsExc(newProduct.getName());
    }

    writeProduct.upsert(newProduct);
  }

  @Override
  public Product getProduct(Long id, String currency) {
    final Product product = loadProduct.byId(id);
    if (!Money.DEFAULT_CURRENCY.equalsIgnoreCase(currency)) {
      final Money convertedPrice = convertCurrency.convertFromEuro(product.getPrice(), currency);
      product.convertCurrency(convertedPrice);
    }
    return product;
  }

  @Override
  public Product getProduct(Long id) {
    return loadProduct.byId(id);
  }

  @Override
  public Set<Product> getProductsByCategory(Long id) {
    return loadProduct.byCategory(id);
  }

  @Override
  public Set<Product> getProductsByCategory(Long id, String currency) {
    final Set<Product> products = loadProduct.byCategory(id);
    if (!Money.DEFAULT_CURRENCY.equalsIgnoreCase(currency)) {
      for (Product product : products) {
        final Money convertedPrice = convertCurrency.convertFromEuro(product.getPrice(), currency);
        product.convertCurrency(convertedPrice);
      }
    }
    return products;
  }

  @Override
  public void updateProduct(UpdateProductCmd updateProductCmd) {
    final Set<Category> categories = loadCategory.byIds(updateProductCmd.categories());
    final Money price = createMoney(updateProductCmd.price(), updateProductCmd.currency());
    final Product product =
        Product.loadExisting(
            updateProductCmd.id(),
            updateProductCmd.name(),
            updateProductCmd.description(),
            price,
            categories);

    if (loadProduct.existsOtherByName(product.getId(), product.getName())) {
      throw new ProductAlreadyExistsExc(product.getName());
    }

    writeProduct.upsert(product);
  }

  @Override
  public void deleteProduct(Long id) {
    writeProduct.delete(id);
  }

  private Money createMoney(BigDecimal price, String currency) {
    Money money = new Money(price, currency);
    if (!Money.DEFAULT_CURRENCY.equalsIgnoreCase(currency)) {
      money = convertCurrency.convertToEuro(money);
    }
    return money;
  }
}
