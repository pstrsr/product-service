package com.github.almostfamiliar.product.web.dto.response;

import lombok.Data;

import java.math.BigInteger;
import java.util.Set;

@Data
public class CategoryResponse {
  private BigInteger id;
  private String name;
  private Set<CategoryResponse> children;
}
