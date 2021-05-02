package com.github.almostfamiliar.port.in;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.command.CreateProductCmd;
import com.github.almostfamiliar.port.command.UpdateProductCmd;
import com.github.almostfamiliar.validators.Currency;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface ProductCRUD {
  void createProduct(@Valid CreateProductCmd product);

  Product getProduct(@NotNull @Min(0) Long id, @NotEmpty @Currency String currency);

  Product getProduct(@NotNull @Min(0) Long id);

  Set<Product> getProductsByCategory(@NotNull @Min(0) Long id);

  Set<Product> getProductsByCategory(@NotNull @Min(0) Long id, @NotBlank @Currency String currency);

  void updateProduct(@Valid UpdateProductCmd updateProductCmd);

  void deleteProduct(@NotNull @Min(0) Long id);
}
