package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.domain.Money;
import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.command.CreateProductCmd;
import com.github.almostfamiliar.port.command.UpdateProductCmd;
import com.github.almostfamiliar.port.in.ProductCRUD;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private ProductCRUD productCRUD;

  @Test
  public void should_CreateProduct() throws Exception {
    final String body = TestUtils.readStringFromResource("product/create_product.json");
    mockMvc
        .perform(post("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn();

    CreateProductCmd createProductCmd =
        new CreateProductCmd(
            "Onesie",
            "Big bear onesie. Super warm!",
            BigDecimal.valueOf(1.99),
            "USD",
            Collections.singletonList(8L));

    then(productCRUD).should().createProduct(eq(createProductCmd));
  }

  @Test
  public void should_UpdateProduct() throws Exception {
    final String body = TestUtils.readStringFromResource("product/update_product.json");
    mockMvc
        .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn();

    UpdateProductCmd updateProductCmd =
        new UpdateProductCmd(
            1L,
            "Onesie",
            "Big bear onesie. Super warm!",
            BigDecimal.valueOf(1.99),
            "USD",
            Collections.singletonList(8L));

    then(productCRUD).should().updateProduct(eq(updateProductCmd));
  }

  @Test
  public void should_GetProductByID() throws Exception {
    final Category category = Category.loadExisting(1L, "clothes", new HashSet<>());
    final Money price = new Money(BigDecimal.valueOf(1.99), "EUR");
    Product product = Product.loadExisting(1L, "Jacket", "very warm", price, category);
    given(productCRUD.getProduct(1L)).willReturn(product);

    mockMvc
        .perform(get("/v1/product/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.name").value("Jacket"))
        .andExpect(jsonPath("$.description").value("very warm"))
        .andExpect(jsonPath("$.price.amount").value(1.99))
        .andExpect(jsonPath("$.price.currency").value("EUR"))
        .andExpect(jsonPath("$.categories[0].id").value(1))
        .andExpect(jsonPath("$.categories[0].name").value("clothes"));
  }

  @Test
  public void should_GetProductByIDWithCurrencyConverted() throws Exception {
    final Category category = Category.loadExisting(1L, "clothes", new HashSet<>());
    final Money price = new Money(BigDecimal.valueOf(1.99), "USD");
    Product product = Product.loadExisting(1L, "Jacket", "very warm", price, category);
    given(productCRUD.getProduct(1L, "USD")).willReturn(product);

    mockMvc
        .perform(
            get("/v1/product/{id}", 1L)
                .queryParam("currency", "USD")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.name").value("Jacket"))
        .andExpect(jsonPath("$.description").value("very warm"))
        .andExpect(jsonPath("$.price.amount").value(1.99))
        .andExpect(jsonPath("$.price.currency").value("USD"))
        .andExpect(jsonPath("$.categories[0].id").value(1))
        .andExpect(jsonPath("$.categories[0].name").value("clothes"));
  }

  @Test
  public void should_GetProductByCategory() throws Exception {
    final Category category = Category.loadExisting(1L, "clothes", new HashSet<>());
    final Money price = new Money(BigDecimal.valueOf(1.99), "EUR");
    Product product = Product.loadExisting(1L, "Jacket", "very warm", price, category);
    given(productCRUD.getProductsByCategory(1L))
        .willReturn(new HashSet<>(Collections.singletonList(product)));

    mockMvc
        .perform(
            get("/v1/products")
                .queryParam("categoryId", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$[0].name").value("Jacket"))
        .andExpect(jsonPath("$[0].description").value("very warm"))
        .andExpect(jsonPath("$[0].price.amount").value(1.99))
        .andExpect(jsonPath("$[0].price.currency").value("EUR"))
        .andExpect(jsonPath("$[0].categories[0].id").value(1))
        .andExpect(jsonPath("$[0].categories[0].name").value("clothes"));
  }

  @Test
  public void should_GetProductByCategoryWithCurrencyConverted() throws Exception {
    final Category category = Category.loadExisting(1L, "clothes", new HashSet<>());
    final Money price = new Money(BigDecimal.valueOf(1.99), "USD");
    Product product = Product.loadExisting(1L, "Jacket", "very warm", price, category);
    given(productCRUD.getProductsByCategory(1L, "USD"))
        .willReturn(new HashSet<>(Collections.singletonList(product)));

    mockMvc
        .perform(
            get("/v1/products")
                .queryParam("categoryId", "1")
                .queryParam("currency", "USD")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$[0].name").value("Jacket"))
        .andExpect(jsonPath("$[0].description").value("very warm"))
        .andExpect(jsonPath("$[0].price.amount").value(1.99))
        .andExpect(jsonPath("$[0].price.currency").value("USD"))
        .andExpect(jsonPath("$[0].categories[0].id").value(1))
        .andExpect(jsonPath("$[0].categories[0].name").value("clothes"));
  }

  @Test
  public void should_DeleteProduct() throws Exception {
    mockMvc
        .perform(
            delete("/v1/product/{id}", 1L)
                .queryParam("categoryId", "1")
                .queryParam("currency", "USD")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    then(productCRUD).should().deleteProduct(1L);
  }
}
