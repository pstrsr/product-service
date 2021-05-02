package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.exception.CategoryNotEmptyExc;
import com.github.almostfamiliar.exception.RootCategoryAlreadyExistsExc;
import com.github.almostfamiliar.exception.SubCategorySameNameAsParentExc;
import com.github.almostfamiliar.port.command.CreateCategoryCmd;
import com.github.almostfamiliar.port.command.UpdateCategoryCmd;
import com.github.almostfamiliar.port.in.CategoryCRUD;
import com.github.almostfamiliar.port.out.LoadCategory;
import com.github.almostfamiliar.port.out.WriteCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CategoryService implements CategoryCRUD {
  private final LoadCategory loadCategory;
  private final WriteCategory writeCategory;

  @Override
  public void createCategory(CreateCategoryCmd createCategoryCmd) {
    final Category newCategory = Category.createNew(createCategoryCmd.name());

    createCategoryCmd
        .parentId()
        .ifPresentOrElse(
            parentId -> createSubCategory(newCategory, parentId),
            () -> createRootCategory(newCategory));
  }

  @Override
  public Category getCategory(Long id) {
    return loadCategory.byId(id);
  }

  @Override
  public Set<Category> getAllCategories() {
    return loadCategory.loadAllRoots();
  }

  @Override
  public void deleteCategory(Long id) {
    if (loadCategory.doesAnyIncomingRelationshipExist(id)) {
      throw new CategoryNotEmptyExc();
    }

    writeCategory.delete(id);
  }

  @Override
  public void updateCategory(UpdateCategoryCmd updateCategoryCmd) {
    final Category category = loadCategory.byId(updateCategoryCmd.id());

    category.changeName(updateCategoryCmd.name());
    updateCategoryCmd
        .parentId()
        .ifPresentOrElse(
            parentId -> updateSubCategory(category, parentId), () -> updateRootCategory(category));
  }

  private void updateRootCategory(Category category) {
    removeCategoryFromOldParentIfExists(category);

    writeCategory.upsert(category);
  }

  private void updateSubCategory(Category category, Long parentId) {
    if (loadCategory.doesParentCategoryExist(parentId, category.getName())) {
      throw new SubCategorySameNameAsParentExc(category.getName());
    }

    final Category newParent = loadCategory.byId(parentId);

    removeCategoryFromOldParentIfExists(category);
    newParent.addChild(category);
    writeCategory.upsert(newParent);
    writeCategory.upsert(category);
  }

  private void removeCategoryFromOldParentIfExists(Category category) {
    final Optional<Category> oldParent = loadCategory.getParent(category.getId());
    oldParent.ifPresent(
        oldParentCategory -> {
          oldParentCategory.removeChild(category);
          writeCategory.upsert(oldParentCategory);
        });
  }

  private void createRootCategory(Category newRootCategory) {
    if (loadCategory.doesRootCategoryExist(newRootCategory.getName())) {
      throw new RootCategoryAlreadyExistsExc(newRootCategory);
    }

    writeCategory.upsert(newRootCategory);
  }

  private void createSubCategory(Category category, Long parentId) {
    if (loadCategory.doesParentCategoryExist(parentId, category.getName())) {
      throw new SubCategorySameNameAsParentExc(category.getName());
    }

    // Order is important, because parent id might be the same as savedNode
    final Category parentNode = loadCategory.byId(parentId);
    final Category savedNode = writeCategory.upsert(category);

    parentNode.addChild(savedNode);

    writeCategory.upsert(parentNode);
  }
}
