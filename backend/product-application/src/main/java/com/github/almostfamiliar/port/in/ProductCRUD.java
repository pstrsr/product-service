package com.github.almostfamiliar.port.in;

import com.github.almostfamiliar.domain.Product;

import java.math.BigInteger;

public interface ProductCRUD {
  void createProduct(Product product);

  Product getProduct(BigInteger id, String currency);

  Product getProduct(BigInteger id);

  void updateProduct(Product product);

  void deleteProduct(BigInteger id);
}
