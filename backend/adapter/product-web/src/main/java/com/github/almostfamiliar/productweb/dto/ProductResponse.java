package com.github.almostfamiliar.productweb.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class ProductResponse {
  private BigInteger id;
  private String name;
  private String description;
  private List<CategoryResponse> categories;
}
