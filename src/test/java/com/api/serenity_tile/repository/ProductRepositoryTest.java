package com.api.serenity_tile.repository;

import com.api.serenity_tile.model.Product;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");
    @Autowired
    ProductRepository productRepository;
    @LocalServerPort
    private String port;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        productRepository.deleteAll();
    }

    /**
     * should get all products
     */
    @Test
    public void shouldGetAllProducts() {
        List<Product> products = List.of(new Product("Product 1", 1F, 2, 3, 4, true), new Product("Product 2", 1F, 2, 3, 4, true));
        productRepository.saveAll(products);

        given().contentType(ContentType.JSON).when().get("/api/products").then().statusCode(200).body(".", hasSize(2));
    }

    /**
     * should get a product
     */
    @Test
    public void shouldGetASingleProduct() {
        List<Product> products = List.of(new Product("Product 1", 1F, 2, 3, 4, true), new Product("Product 2", 1F, 2, 3, 4, true));
        productRepository.saveAll(products);

        Optional<Product> product = productRepository.findById(products.get(0).getId());

        given().contentType(ContentType.JSON).when().get("/api/product/{id}", product.map(p -> p.getId())).then().statusCode(200);
    }
}
