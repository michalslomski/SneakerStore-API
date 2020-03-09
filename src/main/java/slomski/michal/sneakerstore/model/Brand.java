package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private BrandName brandName;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Product> products = new HashSet<>();

    public Brand() {
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public BrandName getBrandName() {
        return brandName;
    }

    public void setBrandName(BrandName brandName) {
        this.brandName = brandName;
    }
}

