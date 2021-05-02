package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Product;

public interface WriteProduct {
  void upsert(Product product);

  void delete(Long id);
}
