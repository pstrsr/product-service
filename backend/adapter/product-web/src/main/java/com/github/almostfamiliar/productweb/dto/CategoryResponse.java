package com.github.almostfamiliar.productweb.dto;

import com.github.almostfamiliar.domain.Category;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CategoryResponse {
  private BigInteger id;
  private String name;
  private Category parent;
}
