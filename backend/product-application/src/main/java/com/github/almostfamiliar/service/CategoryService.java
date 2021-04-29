package com.github.almostfamiliar.service;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.exception.CategoryNotEmptyExc;
import com.github.almostfamiliar.port.in.CategoryCRUD;
import com.github.almostfamiliar.port.out.LoadCategory;
import com.github.almostfamiliar.port.out.LoadProduct;
import com.github.almostfamiliar.port.out.WriteCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryCRUD {
  private final LoadCategory loadCategory;
  private final WriteCategory writeCategory;
  private final LoadProduct loadProduct;

  @Override
  public void createCategory(Category category) {
    writeCategory.upsert(category);
  }

  @Override
  public Category getCategory(BigInteger id, String currency) {
    return loadCategory.byId(id);
  }

  @Override
  public void updateCategory(Category category) {
    writeCategory.upsert(category);
  }

  @Override
  public void deleteCategory(BigInteger id) {
    final Category category = loadCategory.byId(id);
    final List<Product> products = loadProduct.byCategory(category.getId());
    if (products.size() > 0) {
      throw new CategoryNotEmptyExc();
    }
    writeCategory.delete(id);
  }
}
