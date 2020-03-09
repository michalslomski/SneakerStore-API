package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotNull
    private String productName;

    @ManyToOne
    @JsonBackReference
    private Brand brand;

    @NotNull
    private String productPrice;

    private ArrayList<String> sizes;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    @JsonManagedReference
    private Set<Category> categories;

    @JsonBackReference
    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinTable(name = "purchase_product",joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private Set<Purchase> purchaseProducts = new HashSet<>();

    public Product() {
    }

    public Product(@NotNull String productName, Brand brand, @NotNull String productPrice, ArrayList<String> sizes, @NotNull Gender gender, Set<Category> categories) {
        this.productName = productName;
        this.brand = brand;
        this.productPrice = productPrice;
        this.sizes = sizes;
        this.gender = gender;
        this.categories = categories;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<String> getSizes() {
        return sizes;
    }

    public void setSizes(ArrayList<String> sizes) {
        this.sizes = sizes;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Set<Purchase> getPurchaseProducts() {
        return purchaseProducts;
    }

    public void setPurchaseProducts(Set<Purchase> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }
}
