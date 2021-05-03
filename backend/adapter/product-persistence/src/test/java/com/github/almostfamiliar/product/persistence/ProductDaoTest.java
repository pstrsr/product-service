package com.github.almostfamiliar.product.persistence;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.product.persistence.errors.ProductDoesNotExistExc;
import com.github.almostfamiliar.product.persistence.node.ProductNode;
import com.github.almostfamiliar.product.persistence.repository.ProductRepository;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ContextConfiguration(initializers = TestContainerInitializer.class)
@Slf4j
class ProductDaoTest {
  @Autowired private ProductDao productDao;
  @Autowired private ProductRepository productRepository;
  @Autowired private TestRepository testRepository;

  @BeforeEach
  public void setup() {
    // creates same test set as Liquibase migration
    testRepository.createTestData1();
    testRepository.createTestData2();
    testRepository.createTestData3();
    testRepository.createTestData4();
    testRepository.createTestData5();
  }

  @AfterEach
  public void deleteAll() throws InterruptedException {
    testRepository.deleteAllNodes();
    Thread.sleep(2000); // ugly, but tests do not work without this!
  }

  @Test
  public void should_CreateProduct() {
    final Category category = Category.loadExisting(0L, "clothes", new HashSet<>());
    final Product product =
        Product.createNew(
            "Pepper",
            "very warm",
            new Money(BigDecimal.ONE, "EUR"),
            new HashSet<>(Collections.singletonList(category)));
    productDao.upsert(product);

    Set<ProductNode> all = productRepository.findAll();
    log.info(all.toString());
    assertEquals(6, all.size());
  }

  @Test
  public void should_GetAllProductsByCategory() {
    assertEquals(3, productDao.byCategory(0L).size());
  }

  @Test
  public void should_GetProductById() {
    assertNotNull(productDao.byId(9L));
  }

  @Test
  public void should_ThrowDoesNotExistError_When_ProvidingInvalidID() {
    assertThrows(ProductDoesNotExistExc.class, () -> productDao.byId(3L));
  }

  @Test
  public void should_ExistByName() {
    assertTrue(productDao.existsByName("Jacket"));
  }

  @Test
  public void should_NotExistByName() {
    assertFalse(productDao.existsByName("Water slide"));
  }

  @Test
  public void should_NotExistOtherByName() {
    assertFalse(productDao.existsOtherByName(9L, "Jacket"));
  }

  @Test
  public void should_ExistOtherByName() {
    assertTrue(productDao.existsOtherByName(10L, "Jacket"));
  }

  @Test
  public void should_DeleteProduct() {
    productDao.delete(9L);
    assertEquals(4, productRepository.findAll().size());
  }
}
