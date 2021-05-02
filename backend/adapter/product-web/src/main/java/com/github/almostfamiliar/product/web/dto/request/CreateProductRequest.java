package com.github.almostfamiliar.product.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {
  @NotBlank
  @Schema(description = "Name of the product", example = "Jacket", required = true)
  private String name;

  @Schema(description = "Description of the product", example = "Very warm", required = true)
  private String description;

  @DecimalMin(value = "0.00", inclusive = false)
  @Digits(integer = Integer.MAX_VALUE, fraction = 2)
  @Schema(description = "Price of the product", example = "1.99", required = true)
  private BigDecimal price;

  @Schema(
      description =
          "Currency of the price. If no value is provided euro will be assumed. If a value is provided the price will be coverted to euro and saved in the database.",
      example = "USD",
      required = true)
  private String currency;

  @NotEmpty private List<@Min(0) Long> categories;
}
