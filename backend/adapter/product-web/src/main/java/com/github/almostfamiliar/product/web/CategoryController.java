package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.port.in.CategoryCRUD;
import com.github.almostfamiliar.product.web.dto.request.CreateCategoryRequest;
import com.github.almostfamiliar.product.web.dto.request.UpdateCategoryRequest;
import com.github.almostfamiliar.product.web.dto.response.CategoryResponse;
import com.github.almostfamiliar.product.web.error.ErrorHandling;
import com.github.almostfamiliar.product.web.mapper.CategoryWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class CategoryController {
  private final CategoryCRUD categoryCRUD;
  private final CategoryWebMapper categoryWebMapper;

  @Operation(
      summary = "Find category by ID",
      description = "Returns a single category",
      tags = {"category"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid Parameter",
            content =
                @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
        @ApiResponse(
            responseCode = "404",
            description = "Category does not exist",
            content =
                @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
      })
  @GetMapping(
      value = "/v1/category/{id}",
      produces = {"application/json"})
  @ResponseBody
  public CategoryResponse getCategory(
          @Parameter(description="Id of the category",example = "10", required=true)
          @PathVariable @Min(0) Long id) {
      return categoryWebMapper.toResponse(categoryCRUD.getCategory(id));
  }

  @Operation(
      summary = "Find all categories",
      description =
          "Returns all categories in a tree structure. The first level contains only the root categories, which contain all the other categories in their children.",
      tags = {"category"})
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "successful operation",
            content = @Content( array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))))
      })
  @GetMapping(
      value = "/v1/categories",
      produces = {"application/json"})
  public Set<CategoryResponse> getAllCategories() {
    return categoryWebMapper.toResponse(categoryCRUD.getAllCategories());
  }

  @Operation(
      summary = "Create category",
      description =
          "Creates a new sub categorie, if the parentId is specified and a new root categorie otherwise.",
      tags = {"category"})
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "successful operation"),
        @ApiResponse(
          responseCode = "400",
          description = "Invalid parameters",
          content =
          @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
        @ApiResponse(
          responseCode = "404",
          description = "Parent could not be found",
          content =
          @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
        @ApiResponse(
            responseCode = "409",
            description = """
                    - Any of the parent categories has the same name as the category to be created.
                    - Any of the child categories of the immediate parent already have the same name.
                    - Another root category already has the same name.
                    """,
            content = @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
      })
  @PostMapping(value = "/v1/category",
          produces = {"application/json"})
  public void createCategory(
          @RequestBody CreateCategoryRequest createCategoryRequest) {
    categoryCRUD.createCategory(categoryWebMapper.toCmd(createCategoryRequest));
  }

    @Operation(
            summary = "Update Category",
            description =
                    "Updates a category. If the parent node gets changed all of the underlying categories will belong to that new parent category. \n All the rules that apply when creating a new node, apply here aswell" ,
            tags = {"category"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid parameters",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Parent could not be found",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = """
                    - Any of the parent categories has the same name as the category to be created.
                    - Any of the child categories of the immediate parent already have the same name.
                    - Another root category already has the same name.
                    """,
                            content = @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
            })
  @PutMapping(value = "/v1/category",
          produces = {"application/json"})
  public void updateCategory(@RequestBody UpdateCategoryRequest createCategoryRequest) {
    categoryCRUD.updateCategory(categoryWebMapper.toCmd(createCategoryRequest));
  }

    @Operation(
            summary = "Delete category by ID",
            description =
                    "Deletes a category by ID. A category can only be deleted, if it has no products or categories, that reference it.",
            tags = {"category"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid parameters",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category could not be found",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "409",
                            description = """
                    There are still products or subcategories pointing at this category.
                    """,
                            content = @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
            })
  @DeleteMapping(value = "/v1/category/{id}",
          produces = {"application/json"})
  public void deleteCategory(
          @Parameter(description="Id of the category",example = "10", required=true)
          @PathVariable Long id) {
    categoryCRUD.deleteCategory(id);
  }
}
