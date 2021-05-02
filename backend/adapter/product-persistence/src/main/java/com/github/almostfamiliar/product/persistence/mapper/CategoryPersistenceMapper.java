package com.github.almostfamiliar.product.persistence.mapper;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface CategoryPersistenceMapper {
  default Category toDomain(CategoryNode categoryNode) {
    Set<Category> childCategories =
        categoryNode
            .getChildren()
            .map(children -> children.stream().map(this::toDomain).collect(Collectors.toSet()))
            .orElse(new HashSet<>());

    return Category.loadExisting(categoryNode.getId(), categoryNode.getName(), childCategories);
  }

  Set<Category> toDomain(Set<CategoryNode> categoryNode);

  CategoryNode toNode(Category category);

  default Set<CategoryNode> unwrapOptional(Optional<Set<Category>> value) {
    return value
        .map(children -> children.stream().map(this::toNode).collect(Collectors.toSet()))
        .orElse(new HashSet<>());
  }
}
