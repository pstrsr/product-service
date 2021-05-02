package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.exception.CategoryNotEmptyExc;
import com.github.almostfamiliar.exception.NotFoundException;
import com.github.almostfamiliar.port.in.CategoryCRUD;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
public class ErrorTest {
  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryCRUD categoryCRUD;

  @Test
  public void should_RespondWithInternalServerError() throws Exception {
    final String errorMessage = "Unexpected Error!";
    given(categoryCRUD.getCategory(1L)).willThrow(new IllegalStateException(errorMessage));

    mockMvc
        .perform(get("/v1/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void should_RespondWithBadRequest() throws Exception {
    given(categoryCRUD.getCategory(1L))
        .willThrow(new ConstraintViolationException(new HashSet<>()));

    mockMvc
        .perform(get("/v1/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.status").value(400));
  }

  @Test
  public void should_RespondWithNotFound() throws Exception {
    given(categoryCRUD.getCategory(1L)).willThrow(new NotFoundException("Not Found!") {});

    mockMvc
        .perform(get("/v1/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.status").value(404));
  }

  @Test
  public void should_RespondWithConflict() throws Exception {
    given(categoryCRUD.getCategory(1L)).willThrow(new CategoryNotEmptyExc());

    mockMvc
        .perform(get("/v1/category/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(content().contentTypeCompatibleWith("application/json"))
        .andExpect(jsonPath("$.status").value(409));
  }
}
