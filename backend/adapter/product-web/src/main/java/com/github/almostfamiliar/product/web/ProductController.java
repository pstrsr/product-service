package com.github.almostfamiliar.product.web;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.in.ProductCRUD;
import com.github.almostfamiliar.product.web.dto.request.CreateProductRequest;
import com.github.almostfamiliar.product.web.dto.request.UpdateProductRequest;
import com.github.almostfamiliar.product.web.dto.response.ProductResponse;
import com.github.almostfamiliar.product.web.error.ErrorHandling;
import com.github.almostfamiliar.product.web.mapper.ProductWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@RestController
public class ProductController {
  private final ProductCRUD productCRUD;
  private final ProductWebMapper productMapper;

  @Operation(
          summary = "Get product by ID",
          description =
                  "Returns a single product" ,
          tags = {"product"})
  @ApiResponses(
          value = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "successful operation",
                          content = @Content(schema = @Schema(implementation = ProductResponse.class))
                  ),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Invalid parameters",
                          content =
                          @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                  @ApiResponse(
                          responseCode = "404",
                          description = "Product could not be found",
                          content =
                          @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
          })
  @GetMapping(
      value = "/v1/product/{id}",
      produces = {"application/json"})
  public ProductResponse getProduct(
      @Parameter(description="Id of the product",example = "10", required=true)
      @PathVariable Long id,
      @Parameter(description="Currency that the product should be returned in.",example = "USD")
      @RequestParam(value = "currency", required = false) Optional<@Size(min = 3, max = 3)String> currency) {
    final Product product =
        currency.map(curr -> productCRUD.getProduct(id, curr)).orElse(productCRUD.getProduct(id));

    return productMapper.toResponse(product);
  }

  @Operation(
          summary = "Create product",
          description =
                  "Creates a new product",
          tags = {"product"})
  @ApiResponses(
          value = {
                  @ApiResponse(responseCode = "200", description = "successful operation"),
                  @ApiResponse(
                          responseCode = "400",
                          description = "Invalid request",
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
                    An other product already has the same name. Has to be unique.
                    """,
                          content = @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
          })
  @PostMapping(value = "/v1/product",
          produces = {"application/json"})
  public void createProduct(@RequestBody CreateProductRequest createProductRequest) {
    productCRUD.createProduct(productMapper.toCmd(createProductRequest));
  }

    @Operation(
            summary = "Get products by category",
            description =
                    "Gets all products and of a category and all products of its subcatgories",
            tags = {"product"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Category could not be found",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
            })
  @GetMapping(
      value = "/v1/products",
      produces = {"application/json"})
  public Set<ProductResponse> getProductsByCategory(
    @Parameter(description="Id of the category",example = "10", required=true)
    @RequestParam Long categoryId,
    @Parameter(description="Currency that the products should be returned in.",example = "USD")
    @RequestParam(value = "currency", required = false) Optional<@Size(min = 3, max = 3)String> currency) {
        final Set<Product> products = currency
                .map(curr -> productCRUD.getProductsByCategory(categoryId, curr))
                .orElse(productCRUD.getProductsByCategory(categoryId));

        return productMapper.toResponse(products);
  }

    @Operation(
            summary = "Update product",
            description =
                    "Updates a product",
            tags = {"product"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
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
                    An other product already has the same name. Has to be unique.
                    """,
                            content = @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class)))
            })
  @PutMapping(value = "/v1/product",
          produces = {"application/json"})
  public void getProductsByCategory(@RequestBody UpdateProductRequest updateProductRequest) {
    productCRUD.updateProduct(productMapper.toCmd(updateProductRequest));
  }

    @Operation(
            summary = "Delete product",
            description =
                    "Updates a product",
            tags = {"product"})
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "successful operation"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product could not be found",
                            content =
                            @Content(schema = @Schema(implementation = ErrorHandling.ErrorResponse.class))),
            })
    @DeleteMapping(value = "/v1/product/{id}",
          produces = {"application/json"})
  public void deleteProduct(
            @Parameter(description="Id of the product",example = "10", required=true) @PathVariable Long id) {
    productCRUD.deleteProduct(id);
  }
}
