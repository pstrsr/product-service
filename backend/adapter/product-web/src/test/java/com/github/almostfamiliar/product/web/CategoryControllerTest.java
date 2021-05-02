package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.domain.Category;
import com.github.almostfamiliar.port.command.CreateCategoryCmd;
import com.github.almostfamiliar.port.command.UpdateCategoryCmd;
import com.github.almostfamiliar.port.in.CategoryCRUD;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = CategoryController.class)
class CategoryControllerTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryCRUD categoryCRUD;

  @Test
  public void should_CreateCategory() throws Exception {
    final String body = TestUtils.readStringFromResource("category/create_root_category.json");
    mockMvc
        .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn();

    CreateCategoryCmd createCategoryCmd = new CreateCategoryCmd("clothes", Optional.empty());

    then(categoryCRUD).should().createCategory(eq(createCategoryCmd));
  }

  @Test
  public void should_CreateSubCategory() throws Exception {
    final String body = TestUtils.readStringFromResource("category/create_sub_category.json");
    mockMvc
        .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn();

    CreateCategoryCmd createCategoryCmd = new CreateCategoryCmd("woman", Optional.of(1L));

    then(categoryCRUD).should().createCategory(eq(createCategoryCmd));
  }

  @Test
  public void should_UpdateCategory() throws Exception {
    final String body = TestUtils.readStringFromResource("category/update_root_category.json");
    mockMvc
        .perform(put("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andReturn();

    UpdateCategoryCmd createCategoryCmd = new UpdateCategoryCmd(1L, "clothes", Optional.empty());

    then(categoryCRUD).should().updateCategory(eq(createCategoryCmd));
  }

  @Test
  public void should_DeleteCategory() throws Exception {
    mockMvc.perform(delete("/v1/category/{id}", 1L)).andExpect(status().isOk()).andReturn();

    then(categoryCRUD).should().deleteCategory(1L);
  }

  @Test
  public void should_GetCategoryByID() throws Exception {
    given(categoryCRUD.getCategory(1L))
        .willReturn(Category.loadExisting(1L, "clothes", new HashSet<>()));

    mockMvc
        .perform(get("/v1/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.name").value("clothes"))
        .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  public void should_GetAllCategories() throws Exception {
    given(categoryCRUD.getAllCategories())
        .willReturn(new HashSet<>(List.of(Category.loadExisting(1L, "clothes", new HashSet<>()))));

    mockMvc
        .perform(get("/v1/categories").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$[0].name").value("clothes"))
        .andExpect(jsonPath("$[0].id").value(1));
  }
}
