package com.github.almostfamiliar.product.web.dto.response;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class ProductResponse {
  private BigInteger id;
  private String name;
  private String description;
  private MoneyResponse price;
  private List<CategoryResponse> categories;
}
