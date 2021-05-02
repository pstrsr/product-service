package com.github.almostfamiliar.product.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateCategoryRequest {

  @Min(0)
  @Schema(description = "ID of the category", example = "10", required = true)
  private Long id;

  @NotBlank
  @Schema(description = "Name of the category", example = "Clothes", required = true)
  private String name;

  @Schema(description = "ID of the parent category", example = "5")
  private Long parentId;
}
