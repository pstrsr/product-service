package com.github.almostfamiliar.product.persistence;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.product.persistence.errors.CategoryDoesNotExistExc;
import com.github.almostfamiliar.product.persistence.node.CategoryNode;
import com.github.almostfamiliar.product.persistence.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ContextConfiguration(initializers = TestContainerInitializer.class)
@Slf4j
public class CategoryDaoTest {

  @Autowired private CategoryDao categoryDao;
  @Autowired private CategoryRepository categoryRepository;
  @Autowired private TestRepository testRepository;

  @BeforeEach
  public void setup() throws IOException {
    // creates same testset as Liquibase migration
    testRepository.createTestData1();
    testRepository.createTestData2();
    testRepository.createTestData3();
    testRepository.createTestData4();
    testRepository.createTestData5();
  }

  @AfterEach
  public void deleteAll() throws IOException, InterruptedException {
    testRepository.deleteAllNodes();
    Thread.sleep(2000); // ugly, but tests do not work without this!
  }

  @Test
  public void should_InsertCategory() {
    final Category category = Category.createNew("Clothes");
    categoryDao.upsert(category);

    assertEquals(10, categoryRepository.findAll().size());
  }

  @Test
  public void should_ThrowDoesNotExistError_When_ProvidingWrongId() {
    assertThrows(CategoryDoesNotExistExc.class, () -> categoryDao.byId(-1L));
  }

  @Test
  public void should_FindCategoriesByIds() {
    Set<CategoryNode> all = categoryRepository.findAll();
    log.error(all.toString());
    assertEquals(3, categoryDao.byIds(List.of(0L, 2L, 3L)).size());
  }

  @Test
  public void should_FindAllRoots() {
    final Set<Category> roots = categoryDao.loadAllRoots();
    assertEquals(2, roots.size());

    for (Category root : roots) {
      assertNotNull(root.getChildren());
    }
  }

  @Test
  public void should_FindParentWithSameName() {
    assertTrue(categoryDao.doesParentCategoryWithSameNameExist(4L, "women"));
  }

  @Test
  public void should_NotFindParentWithSameName() {
    assertFalse(categoryDao.doesParentCategoryWithSameNameExist(9L, "men"));
  }

  @Test
  public void should_FindRoot() {
    assertTrue(categoryDao.doesRootCategoryExistWithName("clothes"));
  }

  @Test
  public void should_NotFindRoot() {
    assertFalse(categoryDao.doesRootCategoryExistWithName("Laptop"));
  }

  @Test
  public void should_FindIncommingRelationships() {
    assertTrue(categoryDao.doesAnyIncomingRelationshipExist(3L));
  }

  @Test
  public void should_FindParent() {

    assertTrue(categoryDao.findParent(1L).isPresent());
    assertEquals("clothes", categoryDao.findParent(1L).get().getName());
  }

  @Test
  public void should_DeleteCategory() {
    categoryDao.delete(8L);
    assertEquals(8, categoryRepository.findAll().size());
  }
}
