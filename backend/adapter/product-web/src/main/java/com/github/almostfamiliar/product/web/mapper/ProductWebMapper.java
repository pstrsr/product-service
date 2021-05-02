package com.github.almostfamiliar.product.web.mapper;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.command.CreateProductCmd;
import com.github.almostfamiliar.port.command.UpdateProductCmd;
import com.github.almostfamiliar.product.web.dto.request.CreateProductRequest;
import com.github.almostfamiliar.product.web.dto.request.UpdateProductRequest;
import com.github.almostfamiliar.product.web.dto.response.ProductResponse;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(uses = {CategoryWebMapper.class, MoneyWebMapper.class})
public interface ProductWebMapper {
  ProductResponse toResponse(Product product);

  Set<ProductResponse> toResponse(Set<Product> product);

  /** Bug in jdk does not allow automatic mapping. */
  default CreateProductCmd toCmd(CreateProductRequest createProductRequest) {
    return new CreateProductCmd(
        createProductRequest.getName(),
        createProductRequest.getDescription(),
        createProductRequest.getPrice(),
        createProductRequest.getCurrency(),
        createProductRequest.getCategories());
  }

  /** Bug in jdk does not allow automatic mapping. */
  default UpdateProductCmd toCmd(UpdateProductRequest updateProductRequest) {
    return new UpdateProductCmd(
        updateProductRequest.getId(),
        updateProductRequest.getName(),
        updateProductRequest.getDescription(),
        updateProductRequest.getPrice(),
        updateProductRequest.getCurrency(),
        updateProductRequest.getCategories());
  }
}
