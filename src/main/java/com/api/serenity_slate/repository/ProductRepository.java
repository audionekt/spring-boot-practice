package com.api.serenity_slate.repository;

import com.api.serenity_slate.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByCategoriesId(Long categoryId);
}
