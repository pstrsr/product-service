package com.github.almostfamiliar.product.persistence;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.port.out.LoadCategory;
import com.github.almostfamiliar.port.out.WriteCategory;
import com.github.almostfamiliar.product.persistence.errors.CategoryDoesNotExistExc;
import com.github.almostfamiliar.product.persistence.mapper.CategoryPersistenceMapper;
import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import com.github.almostfamiliar.product.persistence.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CategoryDao implements LoadCategory, WriteCategory {
  private final CategoryRepository categoryRepository;
  private final CategoryPersistenceMapper categoryMapper;

  @Override
  public Category byId(Long id) {
    final CategoryNode categoryNode = findById(id);

    return categoryMapper.toDomain(categoryNode);
  }

  @Override
  public Set<Category> byIds(List<Long> ids) {
    // We want to check each id individually, if it really exists.
    return ids.stream().map(this::byId).collect(Collectors.toSet());
  }

  @Override
  public Set<Category> loadAllRoots() {
    // TODO findAllRoots query is bugged - children are null but shouldn't be.
    final Set<CategoryNode> allRootsWithoutChildren = categoryRepository.findAllRoots();
    Set<CategoryNode> allRoots =
        allRootsWithoutChildren.stream()
            .map(CategoryNode::getId)
            .map(this::findById)
            .collect(Collectors.toSet());
    return categoryMapper.toDomain(allRoots);
  }

  @Override
  public boolean doesParentCategoryWithSameNameExist(Long id, String name) {
    return categoryRepository.existsParentByName(id, name);
  }

  @Override
  public boolean doesRootCategoryExistWithName(String name) {
    return categoryRepository.existsRoot(name);
  }

  @Override
  public boolean doesAnyIncomingRelationshipExist(Long id) {
    return categoryRepository.existsAnyIncomingRelationship(id);
  }

  @Override
  public Optional<Category> findParent(Long id) {
    final Optional<CategoryNode> categoryNode = categoryRepository.findParent(id);

    return categoryNode.map(categoryMapper::toDomain);
  }

  @Override
  public Category upsert(Category category) {
    final CategoryNode categoryNode = categoryMapper.toNode(category);
    final CategoryNode savedNode = categoryRepository.save(categoryNode);
    return categoryMapper.toDomain(savedNode);
  }

  @Override
  public void delete(Long id) {
    categoryRepository.delete(findById(id));
  }

  private CategoryNode findById(Long id) {
    return categoryRepository.findById(id).orElseThrow(() -> new CategoryDoesNotExistExc(id));
  }
}
