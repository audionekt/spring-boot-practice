package com.api.serenity_tile.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product")
public class Product {
    public Product(String title, Float pricePerSf, Integer sfPerPc, Integer pcsPerBox, Integer pricePerBox, boolean b) {
        this.title = title;
        this.price_per_sf = pricePerSf;
        this.sf_per_pc = sfPerPc;
        this.pcs_per_box = pcsPerBox;
        this.price_per_box = pricePerBox;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    /**
     * the title / name of the product
     */
    @Column(name = "title", unique = true)
    private String title;

    /**
     * price of a single square foot of the product
     */
    @Column(name = "price_per_sf")
    private Float price_per_sf;

    /**
     * if available by the piece, the square footage contained in a single piece of the product
     */
    @Column(name = "sf_per_pc")
    private Integer sf_per_pc;

    /**
     * if available by the piece + box, the number of pieces in a box
     */
    @Column(name = "pcs_per_box")
    private Integer pcs_per_box;

    /**
     * if available by the box, the price of a single box;
     */
    @Column(name = "price_per_box")
    private Integer price_per_box;

    /**
     * a product can belong to many categories,
     * a category can have many products
     */
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;

    public void addToCategory(Category category) {
        this.categories.add(category);
        category.getProducts().add(this);
    }

    public void removeFromCategory(long categoryId) {
        Category category = this.categories.stream().filter(t -> t.getId() == categoryId).findFirst().orElse(null);
        if (category != null) {
            this.categories.remove(category);
            category.getProducts().remove(this);
        }
    }
}
