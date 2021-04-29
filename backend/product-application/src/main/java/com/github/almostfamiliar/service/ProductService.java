package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.in.ProductCRUD;
import com.github.almostfamiliar.port.out.LoadProduct;
import com.github.almostfamiliar.port.out.WriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductCRUD {
  private final LoadProduct loadProduct;
  private final WriteProduct writeProduct;
  //  private final ConvertCurrency convertCurrency;

  @Override
  public void createProduct(Product product) {
    writeProduct.upsert(product);
  }

  @Override
  public Product getProduct(BigInteger id, String currency) {
    /*    final Money convertedPrice = convertCurrency.convert(product.getPrice());
    product.convertCurrency(convertedPrice);*/
    return loadProduct.byId(id);
  }

  @Override
  public Product getProduct(BigInteger id) {
    return loadProduct.byId(id);
  }

  @Override
  public void updateProduct(Product product) {
    writeProduct.upsert(product);
  }

  @Override
  public void deleteProduct(BigInteger id) {
    writeProduct.delete(id);
  }
}
