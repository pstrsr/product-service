package com.github.almostfamiliar.port.in;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.port.command.CreateCategoryCmd;
import com.github.almostfamiliar.port.command.UpdateCategoryCmd;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

public interface CategoryCRUD {
  void createCategory(@Valid CreateCategoryCmd category);

  Category getCategory(@NotNull @Min(0) Long id); // TODO check if validation works

  Set<Category> getAllCategories();

  void updateCategory(@Valid UpdateCategoryCmd toCmd);

  void deleteCategory(@NotNull @Min(0) Long id);
}
