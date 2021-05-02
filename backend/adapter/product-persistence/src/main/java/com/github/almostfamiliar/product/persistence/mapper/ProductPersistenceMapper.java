package com.github.almostfamiliar.product.persistence.mapper;

import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import com.github.almostfamiliar.product.persistence.node.ProductNode;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = CategoryPersistenceMapper.class)
public interface ProductPersistenceMapper {

  ProductNode toNode(Product product);

  /** Record mapping is bugged in jdk 16. */
  default BigDecimal toDecimalValue(Money money) {
    return money.amount();
  }

  /** Record mapping is bugged in jdk 16. */
  default Money toMoney(BigDecimal price) {
    return new Money(price, "EUR");
  }

  default Product toDomain(ProductNode productNode, CategoryPersistenceMapper categoryMapper) {
    Set<CategoryNode> categories = productNode.getCategories();

    return Product.loadExisting(
        productNode.getId(),
        productNode.getName(),
        productNode.getDescription(),
        toMoney(productNode.getPrice()),
        categoryMapper.toDomain(categories));
  }

  default Set<Product> toDomain(
      Set<ProductNode> productNodes, CategoryPersistenceMapper categoryMapper) {

    return productNodes.stream()
        .map(productNode -> this.toDomain(productNode, categoryMapper))
        .collect(Collectors.toSet());
  }
}
