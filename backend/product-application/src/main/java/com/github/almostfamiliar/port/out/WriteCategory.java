package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Product;

import java.math.BigInteger;

public interface WriteCategory {
  void upsert(Category category);

  void delete(BigInteger id);
}
