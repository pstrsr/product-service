package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LoadCategory {
  Category byId(Long id);

  Set<Category> byIds(List<Long> id);

  Set<Category> loadAllRoots();

  boolean doesParentCategoryExist(Long id, String name);

  boolean doesRootCategoryExist(String name);

  boolean doesAnyIncomingRelationshipExist(Long id);

  Optional<Category> getParent(Long id);
}
