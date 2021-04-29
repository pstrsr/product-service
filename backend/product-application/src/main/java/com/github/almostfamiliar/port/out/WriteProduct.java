package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Product;

import java.math.BigInteger;

public interface WriteProduct {
  void upsert(Product product);

  void delete(BigInteger id);
}
