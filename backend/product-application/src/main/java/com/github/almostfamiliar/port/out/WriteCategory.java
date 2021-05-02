package com.github.almostfamiliar.port.out;

import com.github.almostfamiliar.domain.Category;

public interface WriteCategory {
  Category upsert(Category category);

  void delete(Long id);
}
