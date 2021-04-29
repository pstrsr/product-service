package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Category;

import java.math.BigInteger;

public interface LoadCategory {
  Category byId(BigInteger id);
}
