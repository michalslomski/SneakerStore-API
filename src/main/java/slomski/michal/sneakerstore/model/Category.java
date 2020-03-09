package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoryName categoryName;

    @ManyToOne
    @JsonBackReference
    private Product product;

    public Category() {
    }


    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CategoryName getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(CategoryName categoryName) {
        this.categoryName = categoryName;
    }
}
