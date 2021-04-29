package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Product;

import java.math.BigInteger;
import java.util.List;

public interface LoadProduct {
  Product byId(BigInteger id);

  List<Product> byCategory(BigInteger categoryId);
}
