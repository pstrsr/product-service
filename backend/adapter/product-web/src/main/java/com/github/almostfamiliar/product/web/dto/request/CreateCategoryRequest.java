package com.github.almostfamiliar.product.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CreateCategoryRequest {
  @Schema(
      description = "Name of the category to be created",
      example = "Electronics",
      required = true)
  private String name;

  @Schema(
      description = "ID of the parent category. If not provided a root category will be created.",
      example = "10")
  private Long parentId;
}
