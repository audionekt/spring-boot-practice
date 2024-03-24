package com.api.serenity_slate.controller;

import com.api.serenity_slate.model.Product;
import com.api.serenity_slate.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequestMapping("/api")
@RestController
public class ProductController {
    @Autowired
    ProductRepository productRepository;

    /**
     * get all products
     * @return
     */
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = new ArrayList<Product>();
        productRepository.findAll().forEach(products::add);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    /**
     * create a new product
     * @return
     */
    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestBody @NotNull Product product) {
        Product _product = productRepository.save(new Product(product.getTitle(), product.getPrice_per_sf(), product.getSf_per_pc(), product.getPcs_per_box(), product.getPrice_per_box(), true));
        return new ResponseEntity<>(_product, HttpStatus.CREATED);
    }


    /**
     * get a product by id
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<Optional<Product>> getProduct(@PathVariable("id") Long id) {
        Optional<Product> product = productRepository.findById(id);
        return new ResponseEntity<Optional<Product>>(product, HttpStatus.OK);
    }


    /**
     * update a product by id
     * @param id
     * @param product
     * @return
     */
    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") long id, @RequestBody @NotNull Product product) {
        Product _product = productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No product found with that id"));

        _product.setTitle(product.getTitle());
        _product.setPrice_per_sf(product.getPrice_per_sf());
        _product.setSf_per_pc(product.getSf_per_pc());
        _product.setPcs_per_box(product.getPcs_per_box());
        _product.setPrice_per_box(product.getPrice_per_box());

        return new ResponseEntity<Product>(productRepository.save(_product), HttpStatus.OK);
    }


    /**
     * delete a product by id
     * @param id
     * @return
     */
    @DeleteMapping("/product/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") long id) {
        productRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * delete all products from the database
     * @return
     */
    @DeleteMapping("/products")
    public ResponseEntity<HttpStatus> deleteAllProducts() {
        productRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
