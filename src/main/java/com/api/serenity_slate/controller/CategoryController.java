package com.api.serenity_slate.controller;

import com.api.serenity_slate.model.Category;
import com.api.serenity_slate.model.Product;
import com.api.serenity_slate.repository.CategoryRepository;
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
@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * get all categories
     * @return
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = new ArrayList<Category>();

        categoryRepository.findAll().forEach(categories::add);

        return new ResponseEntity<List<Category>>(categories, HttpStatus.OK);
    }

    /**
     * get all products for a given category
     * @param categoryId
     * @return
     */
    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<Product>> getAllProductsForACategory(@PathVariable(value = "categoryId") Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("no category with that id exists");
        }

        List<Product> products = productRepository.findProductsByCategoriesId(categoryId);
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    /**
     * create an empty category
     *
     * @param category
     * @return
     */
    @PostMapping("/category")
    public ResponseEntity<Category> createNewCategory(@RequestBody @NotNull Category category) {
        Category _category = categoryRepository.save(new Category(category.getName(), true));

        return new ResponseEntity<Category>(_category, HttpStatus.CREATED);
    }

    /**
     * add a product to a category
     * - create the category if it doesn't exist
     * @param productId
     * @param categoryRequest
     * @return
     */
    @PostMapping("/product/{productId}/category")
    public ResponseEntity<Optional<Category>> addProductToCategory(@PathVariable(value = "productId") Long productId, @RequestBody Category categoryRequest) {

        Optional<Category> category = productRepository.findById(productId).map(product -> {
            long categoryId = categoryRequest.getId();
            if (categoryId != 0L) {
                Category _category = categoryRepository.findById(categoryId).orElse(categoryRequest);
                product.addToCategory(_category);
                productRepository.save(product);
                return _category;
            }

            // add and create new category
            product.addToCategory(categoryRequest);
            return categoryRepository.save(categoryRequest);
        });

        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    /**
     * remove a product from a category
     * @param productId
     * @param categoryId
     * @return
     */
    @DeleteMapping("/product/{productId}/category/{categoryId}")
    public ResponseEntity<HttpStatus> deleteProductFromCategory(@PathVariable(value = "productId") Long productId, @PathVariable(value = "categoryId") Long categoryId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("No product with that id exists in the database"));

        product.removeFromCategory(categoryId);
        productRepository.save(product);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * update a category by id
     * @param categoryId
     * @param categoryBody
     * @return
     */
    @PutMapping("/category/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable(value = "categoryId") Long categoryId, @RequestBody @NotNull Category categoryBody) {
        Category _category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("No category with that id exists in the database"));
        _category.setName(categoryBody.getName());
        return new ResponseEntity<Category>(categoryRepository.save(_category), HttpStatus.OK);
    }

    /**
     * delete a category by id
     * @param categoryId
     * @return
     */
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<HttpStatus> deleteCategory(@PathVariable(value = "categoryId") Long categoryId) {
        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * delete all categories
     * @return
     */
    @DeleteMapping("/categories")
    public ResponseEntity<HttpStatus> deleteAllCategories() {
        categoryRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
