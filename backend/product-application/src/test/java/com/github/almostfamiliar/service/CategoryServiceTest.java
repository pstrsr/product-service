package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.exception.CategoryNotEmptyExc;
import com.github.almostfamiliar.exception.RootCategoryAlreadyExistsExc;
import com.github.almostfamiliar.exception.SubCategorySameNameAsParentExc;
import com.github.almostfamiliar.port.command.CreateCategoryCmd;
import com.github.almostfamiliar.port.command.UpdateCategoryCmd;
import com.github.almostfamiliar.port.out.*;
import com.github.almostfamiliar.validators.Currency;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CategoryServiceTest {
  @Autowired private CategoryService sut;
  @MockBean private LoadProduct loadProduct;
  @MockBean private WriteProduct writeProduct;
  @MockBean private LoadCategory loadCategory;
  @MockBean private WriteCategory writeCategory;
  @MockBean private ConvertCurrency convertCurrency;
  @MockBean private Currency.CurrencyValidator currencyValidator;
  @MockBean private ValidateCurrency validateCurrency;

  @Test
  public void should_CreateRootCategory() {
    final String categoryName = "Clothes";
    final CreateCategoryCmd createCategoryCmd =
        new CreateCategoryCmd(categoryName, Optional.empty());

    given(loadCategory.doesRootCategoryExistWithName(categoryName)).willReturn(false);

    sut.createCategory(createCategoryCmd);

    verify(writeCategory, times(1)).upsert(any());
  }

  @Test
  public void should_ThrowRootAlreadyExists_When_ProvidingDuplicateName() {
    final String categoryName = "Clothes";
    final CreateCategoryCmd createCategoryCmd =
        new CreateCategoryCmd(categoryName, Optional.empty());

    given(loadCategory.doesRootCategoryExistWithName(categoryName)).willReturn(true);

    assertThrows(RootCategoryAlreadyExistsExc.class, () -> sut.createCategory(createCategoryCmd));
  }

  @Test
  public void should_CreateSubCategory_When_ProvidingParentId() {
    final long parentCategoryID = 1L;
    final Category parentCategory =
        Category.loadExisting(parentCategoryID, "Clothes", new HashSet<>());
    final String categoryName = "Women";
    final CreateCategoryCmd createCategoryCmd =
        new CreateCategoryCmd(categoryName, Optional.of(parentCategoryID));
    given(loadCategory.doesParentCategoryWithSameNameExist(parentCategoryID, categoryName))
        .willReturn(false);
    given(loadCategory.byId(parentCategoryID)).willReturn(parentCategory);

    sut.createCategory(createCategoryCmd);

    verify(writeCategory, times(2)).upsert(any());
  }

  @Test
  public void should_ThrowSubCategoryHasSameNameAsParent_When_SameNameExistsInParentTree() {
    final long parentCategoryID = 1L;
    final Category parentCategory =
        Category.loadExisting(parentCategoryID, "Clothes", new HashSet<>());
    final String categoryName = "Women";
    final CreateCategoryCmd createCategoryCmd =
        new CreateCategoryCmd(categoryName, Optional.of(parentCategoryID));
    given(loadCategory.doesParentCategoryWithSameNameExist(parentCategoryID, categoryName))
        .willReturn(true);
    given(loadCategory.byId(parentCategoryID)).willReturn(parentCategory);

    assertThrows(SubCategorySameNameAsParentExc.class, () -> sut.createCategory(createCategoryCmd));
  }

  @Test
  public void should_GetCategoryById() {
    final long categoryId = 1L;

    sut.getCategory(categoryId);

    verify(loadCategory, times(1)).byId(categoryId);
  }

  @Test
  public void should_GetAllRootCategories() {

    sut.getAllCategories();

    verify(loadCategory, times(1)).loadAllRoots();
  }

  @Test
  public void should_DeleteCategory_When_NoReferencesExist() {
    final long categoryId = 1L;

    given(loadCategory.doesAnyIncomingRelationshipExist(categoryId)).willReturn(false);

    sut.deleteCategory(1L);

    verify(writeCategory, times(1)).delete(categoryId);
  }

  @Test
  public void should_ThrowCategoryNotEmptyError_When_DeleteingCategoryAndReferencesExist() {
    final long categoryId = 1L;

    given(loadCategory.doesAnyIncomingRelationshipExist(categoryId)).willReturn(true);

    assertThrows(CategoryNotEmptyExc.class, () -> sut.deleteCategory(1L));
  }

  @Test
  public void should_UpdateCategory() {
    final long newParentId = 3L;
    final Category newParentCategory =
        Category.loadExisting(newParentId, "Winter", new HashSet<>());
    final String categoryName = "Women";
    final long categoryID = 2L;
    final UpdateCategoryCmd updateCategory =
        new UpdateCategoryCmd(categoryID, categoryName, Optional.of(newParentId));
    final Category currentCategory =
        Category.loadExisting(categoryID, categoryName, new HashSet<>());
    final long oldParentId = 1L;
    final Category oldParentCategory =
        Category.loadExisting(
            oldParentId, "Clothes", new HashSet<>(Arrays.asList(currentCategory)));

    given(loadCategory.doesParentCategoryWithSameNameExist(newParentId, categoryName))
        .willReturn(false);
    given(loadCategory.byId(newParentId)).willReturn(newParentCategory);
    given(loadCategory.findParent(categoryID)).willReturn(Optional.of(oldParentCategory));
    given(loadCategory.byId(categoryID)).willReturn(currentCategory);

    sut.updateCategory(updateCategory);

    verify(writeCategory, times(3)).upsert(any());
  }

  @Test
  public void should_ThrowDuplicateNameError_When_NameExistsInParentTree() {
    final long newParentId = 3L;
    final Category newParentCategory =
        Category.loadExisting(newParentId, "Winter", new HashSet<>());
    final String categoryName = "Women";
    final long categoryID = 2L;
    final UpdateCategoryCmd updateCategory =
        new UpdateCategoryCmd(categoryID, categoryName, Optional.of(newParentId));
    final Category currentCategory =
        Category.loadExisting(categoryID, categoryName, new HashSet<>());
    final long oldParentId = 1L;
    final Category oldParentCategory =
        Category.loadExisting(
            oldParentId, "Clothes", new HashSet<>(Arrays.asList(currentCategory)));

    given(loadCategory.doesParentCategoryWithSameNameExist(newParentId, categoryName))
        .willReturn(true);
    given(loadCategory.byId(newParentId)).willReturn(newParentCategory);
    given(loadCategory.findParent(categoryID)).willReturn(Optional.of(oldParentCategory));
    given(loadCategory.byId(categoryID)).willReturn(currentCategory);

    assertThrows(SubCategorySameNameAsParentExc.class, () -> sut.updateCategory(updateCategory));
  }

  @Test
  public void should_UpdateRootCategory() {
    final String categoryName = "Women";
    final long categoryID = 2L;
    final UpdateCategoryCmd updateCategory =
        new UpdateCategoryCmd(categoryID, categoryName, Optional.empty());
    final Category currentCategory =
        Category.loadExisting(categoryID, categoryName, new HashSet<>());
    final long oldParentId = 1L;
    final Category oldParentCategory =
        Category.loadExisting(
            oldParentId, "Clothes", new HashSet<>(Arrays.asList(currentCategory)));

    given(loadCategory.findParent(categoryID)).willReturn(Optional.of(oldParentCategory));
    given(loadCategory.byId(categoryID)).willReturn(currentCategory);

    sut.updateCategory(updateCategory);

    verify(writeCategory, times(2)).upsert(any());
  }
}
