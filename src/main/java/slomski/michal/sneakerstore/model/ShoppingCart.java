package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingCartId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "shopping_cart_product",joinColumns = @JoinColumn(name = "basket_id"),
    inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> shoppingCartProducts = new HashSet<>();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    private User user;

    public ShoppingCart() {
    }

    public ShoppingCart( User user) {
        this.user = user;
    }

    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Product> getShoppingCartProducts() {
        return shoppingCartProducts;
    }

    public void setShoppingCartProducts(Set<Product> shoppingCartProducts) {
        this.shoppingCartProducts = shoppingCartProducts;
    }
}

