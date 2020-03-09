package slomski.michal.sneakerstore.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "purchase")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne
    @JsonBackReference
    private User user;


    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonBackReference
    private Address deliveryAddress;

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    private Set<Product> products = new HashSet<>();
    public Purchase() {
    }

    public Purchase(User user, Address deliveryAddress, Set<Product> products) {
        this.user = user;
        this.deliveryAddress = deliveryAddress;
        this.products = products;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product){
        products.add(product);
        product.getPurchaseProducts().add(this);
    }
}
