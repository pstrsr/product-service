package com.github.almostfamiliar.product.web.mapper;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.port.command.CreateCategoryCmd;
import com.github.almostfamiliar.port.command.UpdateCategoryCmd;
import com.github.almostfamiliar.product.web.dto.request.CreateCategoryRequest;
import com.github.almostfamiliar.product.web.dto.request.UpdateCategoryRequest;
import com.github.almostfamiliar.product.web.dto.response.CategoryResponse;
import org.mapstruct.Mapper;

import java.util.Optional;
import java.util.Set;

@Mapper
public interface CategoryWebMapper {
  CategoryResponse toResponse(Category product);

  Set<CategoryResponse> toResponse(Set<Category> product);

  default <T> T mapOptional(Optional<T> optional) {
    return optional.orElse(null);
  }

  /** Mapping with records in other jars is currently bugged. Will be fixed in java 17. */
  default CreateCategoryCmd toCmd(CreateCategoryRequest createCategoryRequest) {
    return new CreateCategoryCmd(
        createCategoryRequest.getName(), Optional.ofNullable(createCategoryRequest.getParentId()));
  }

  /** Mapping with records in other jars is currently bugged. Will be fixed in java 17. */
  default UpdateCategoryCmd toCmd(UpdateCategoryRequest updateCategoryRequest) {
    return new UpdateCategoryCmd(
        updateCategoryRequest.getId(),
        updateCategoryRequest.getName(),
        Optional.ofNullable(updateCategoryRequest.getParentId()));
  }
}
