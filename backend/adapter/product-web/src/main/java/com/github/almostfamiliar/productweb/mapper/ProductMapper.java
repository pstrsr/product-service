package com.github.almostfamiliar.productweb.mapper;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.productweb.dto.ProductResponse;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
  ProductResponse toResponse(Product product);
}
