package com.github.almostfamiliar.productweb;

import com.github.almostfamiliar.domain.Product;
import com.github.almostfamiliar.port.in.ProductCRUD;
import com.github.almostfamiliar.productweb.dto.ProductResponse;
import com.github.almostfamiliar.productweb.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class ProductController {
  private final ProductCRUD productCRUD;
  private final ProductMapper productMapper;

  @GetMapping("/v1/product/{id}")
  public ProductResponse getProduct(
      @PathVariable BigInteger id,
      @RequestParam(value = "currency", required = false) Optional<String> currency) {

    final Product product =
        currency.map(curr -> productCRUD.getProduct(id, curr)).orElse(productCRUD.getProduct(id));

    return productMapper.toResponse(product);
  }
}
