package com.api.serenity_tile.repository;

import com.api.serenity_tile.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByProductsId(Long productId);
}
