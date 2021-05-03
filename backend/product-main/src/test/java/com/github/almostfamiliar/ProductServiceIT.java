package com.github.almostfamiliar;

import com.github.almostfamiliar.product.web.dto.response.ProductResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ContextConfiguration(initializers = TestContainerInitializer.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceIT {

  @Autowired private WebApplicationContext context;

  /**
   * This test reads the generated json document by swagger and writes it to the file system.
   *
   * API generators can autogenerate TypeScript files from this file to call this api from the frontend.
   */
  @Test
  public void generateSwaggerJson() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
        .perform(get("/v3/api-docs").accept(MediaType.APPLICATION_JSON))
        .andDo(
            (result) -> FileUtils.writeStringToFile(
                new File("swagger.json"), result.getResponse().getContentAsString(), "UTF-8"));
  }

  @Test
  @Order(100)
  public void should_GetAllProductsByCategory() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
        .perform(get("/v1/products").param("categoryId", "5").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
  }

  @Test
  @Order(200)
  public void should_GetAllCategories() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
        .perform(get("/v1/categories").accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @Order(300)
  public void should_DenyCategoryWomanInSummer_When_WomanIsImediateParent() throws Exception {
    var body = """
           {
               "name": "women",
               "parentId": 6
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
        .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isConflict());
  }

  @Test
  @Order(400)
  public void should_DenyCategoryClothesInSport_When_AnyCategoryInParentsWithSameNameExists() throws Exception {
    var body = """
           {
               "name": "clothes",
               "parentId": 8
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
  }

  @Test
  @Order(500)
  public void should_DenySportInWinter_When_SubcategoryAlreadyExists() throws Exception {
    var body = """
           {
               "name": "sport",
               "parentId": 7
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
  }


  @Test
  @Order(600)
  public void should_DenyProduct_When_AnyProductWithSameNameAlreadyExists() throws Exception {
    var body = """
           {
                   "name": "iPad",
                   "description": "Pretty!",
                   "price": 1.50,
                   "currency": "EUR",
                   "categories": [10]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
  }

  @Test
  @Order(700)
  public void should_DenyDelete_When_AnyReferencesOnCategoryExist() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(delete("/v1/category/{id}", 5))
            .andExpect(status().isConflict());
  }


  @Test
  @Order(800)
  public void should_ChangeCategoryToRoot_When_NotProvidingParentWithPut() throws Exception {
    var body = """
           {
               "id": 7,
               "name": "winter"
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(put("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk()).andDo(print()).andReturn();

    mockMvc
            .perform(get("/v1/categories").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
    .andExpect(jsonPath("$.length()").value(3));


    var bodyRevertChange = """
           {
               "id": 7,
               "name": "winter",
               "parentId": 5
           }
            """;
    mockMvc
            .perform(put("/v1/category").contentType(MediaType.APPLICATION_JSON).content(bodyRevertChange))
            .andExpect(status().isOk());

    mockMvc
            .perform(get("/v1/categories").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  @Order(900)
  public void should_CreateProduct_And_DeleteItAfter() throws Exception {
    var body = """
           {
               "name": "Dell",
               "description": "Very performant!",
               "price": 1.50,
               "currency": "EUR",
               "categories": [12]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isOk());

    String products = mockMvc
            .perform(get("/v1/products").param("categoryId", "12").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andReturn().getResponse().getContentAsString();

    BigInteger productId = findProductIdByName(products,"Dell");

    ResultActions resultActions = mockMvc
            .perform(delete("/v1/product/{id}",productId))
            .andExpect(status().isOk());
    log.info(resultActions.andReturn().getResponse().getContentAsString());

    mockMvc
            .perform(get("/v1/products").param("categoryId", "12").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1));

  }


  @Test
  @Order(1000)
  public void should_CreateProductInUSD_And_BeRetrievableInUSDAndEUR() throws Exception {
    var body = """
           {
               "name": "Dell",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "USD",
               "categories": [12]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
            .andDo(print())
            .andExpect(status().isOk());

    String productsInLaptop = mockMvc
            .perform(get("/v1/products").param("categoryId", "12").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    BigInteger productId = findProductIdByName(productsInLaptop,"Dell");

    mockMvc
            .perform(get("/v1/product/{id}",productId).param("currency", "USD").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price.currency").value("USD"));

    mockMvc
            .perform(get("/v1/product/{id}",productId).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.price.currency").value("EUR"));

     mockMvc
            .perform(delete("/v1/product/{id}", productId))
            .andExpect(status().isOk());
  }

  @Test
  @Order(1100)
  public void should_GetAllProductsInCHF() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    String productsInLaptop = mockMvc
            .perform(get("/v1/products").param("categoryId", "4").param("currency", "CHF").accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    final Type productsResponseType = new TypeToken<List<ProductResponse>>() {
    }.getType();
    final List<ProductResponse> productResponse = new Gson().fromJson(productsInLaptop, productsResponseType);

    final boolean allCurrenciesAreCHF = productResponse.stream()
            .allMatch(product -> product.getPrice().getCurrency().equalsIgnoreCase("CHF"));

    assertTrue(allCurrenciesAreCHF);
  }

  @Test
  @Order(1200)
  public void should_DenyProductUpdate_When_OtherProductWithSameNameExists() throws Exception {
    var body = """
           {
               "id": 19,
               "name": "Surface",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "EUR",
               "categories": [12]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
            .andDo(print())
            .andExpect(status().isConflict());
  }

  @Test
  @Order(1300)
  public void should_UpdateProduct() throws Exception {
    var body = """
           {
               "id": 19,
               "name": "iPad Pro",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "EUR",
               "categories": [12]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(body))
            .andDo(print())
            .andExpect(status().isOk());

    var bodyRevert = """
           {
               "id": 19,
               "name": "iPad",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "EUR",
               "categories": [12]
           }
            """;
    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(bodyRevert))
            .andDo(print())
            .andExpect(status().isOk());
  }

  @Test
  @Order(1400)
  public void should_NotFindCategoryAndProduct() throws Exception {
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
            .perform(get("/v1/product/{id}", 2321321).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());


    mockMvc
            .perform(get("/v1/category/{id}", 2321321).accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
  }

  @Test
  @Order(1500)
  public void should_RefuseRequestWithInvalidCurrency() throws Exception {
    var bodyABC = """
           {
               "id": 19,
               "name": "iPad Pro",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "ABC",
               "categories": [12]
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(bodyABC))
            .andDo(print())
            .andExpect(status().isBadRequest());

    var bodyBitcoin = """
           {
               "id": 19,
               "name": "iPad Pro",
               "description": "Very performant!",
               "price": 1.80,
               "currency": "Bitcoin",
               "categories": [12]
           }
            """;

    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(bodyBitcoin))
            .andDo(print())
            .andExpect(status().isBadRequest());

    var bodyInvalidDecimals = """
           {
               "id": 19,
               "name": "iPad Pro",
               "description": "Very performant!",
               "price": 1.82130,
               "currency": "EUR",
               "categories": [12]
           }
            """;

    mockMvc
            .perform(put("/v1/product").contentType(MediaType.APPLICATION_JSON).content(bodyInvalidDecimals))
            .andDo(print())
            .andExpect(status().isBadRequest());
  }

  @Test
  @Order(300)
  public void should_DenyRootCategory_When_OneWithSameNameAlreadyExists() throws Exception {
    var body = """
           {
               "name": "clothes"
           }
            """;
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(post("/v1/category").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isConflict());
  }

  @Test
  @Order(Integer.MAX_VALUE)
  public void should_DeleteCategory_After_DeletingChilds() throws Exception {

    // Deleting Shirt & Casual
    MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    mockMvc
            .perform(delete("/v1/product/{id}", 17))
            .andExpect(status().isOk());

    mockMvc
            .perform(delete("/v1/category/{id}", 9))
            .andExpect(status().isOk());


  }

  private BigInteger findProductIdByName(String productsInLaptop, String name) {
    Type productsResponseType = new TypeToken<List<ProductResponse>>() {
    }.getType();
    List<ProductResponse> productResponse = new Gson().fromJson(productsInLaptop, productsResponseType);

    Optional<ProductResponse> dell = productResponse.stream().filter(product -> product.getName().equalsIgnoreCase(name)).findAny();

    assertTrue(dell.isPresent());
    return dell.get().getId();
  }
}
