package com.github.almostfamiliar.product.persistence;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.out.LoadProduct;
import com.github.almostfamiliar.port.out.WriteProduct;
import com.github.almostfamiliar.product.persistence.errors.ProductDoesNotExistExc;
import com.github.almostfamiliar.product.persistence.mapper.CategoryPersistenceMapper;
import com.github.almostfamiliar.product.persistence.mapper.ProductPersistenceMapper;
import com.github.almostfamiliar.product.persistence.node.ProductNode;
import com.github.almostfamiliar.product.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductDao implements LoadProduct, WriteProduct {
  private final ProductRepository productRepository;
  private final ProductPersistenceMapper productMapper;
  private final CategoryPersistenceMapper categoryMapper;

  @Override
  public Product byId(Long id) {
    final ProductNode productNode =
        productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistExc(id));

    return productMapper.toDomain(productNode, categoryMapper);
  }

  @Override
  public Set<Product> byCategory(Long categoryId) {
    final Set<ProductNode> productNodes = productRepository.findAllByCategory(categoryId);

    return productMapper.toDomain(productNodes, categoryMapper);
  }

  @Override
  public void upsert(Product product) {
    final ProductNode productNode = productMapper.toNode(product);
    productRepository.save(productNode);
  }

  @Override
  public boolean existsByName(String name) {
    return productRepository.existsByName(name);
  }

  @Override
  public boolean existsOtherByName(Long id, String name) {
    return productRepository.existsOtherByName(id, name);
  }

  @Override
  public void delete(Long id) {
    productRepository.delete(findById(id));
  }

  private ProductNode findById(Long id) {
    return productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistExc(id));
  }
}
