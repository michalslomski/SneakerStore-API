package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Size(min=5)
    @Email
    private String emailAddress;

    @Size(min=6)
    @JsonIgnore
    private String password;

    @Size(min=6)
    @Transient
    @JsonIgnore
    private String passwordConfirm;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private Set<ShoppingCart> shoppingCarts = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    @JsonManagedReference
    private Set<Purchase> purchases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }


    public User(String emailAddress, String password, String firstName, String lastName,Collection<? extends GrantedAuthority> authorities) {
        this.emailAddress = emailAddress;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }



    @ElementCollection(targetClass=GrantedAuthority.class)
    public Collection<? extends GrantedAuthority> authorities;



    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        this.addresses = addresses;
    }

    public Set<ShoppingCart> getShoppingCarts() {
        return shoppingCarts;
    }

    public void setShoppingCarts(Set<ShoppingCart> shoppingCarts) {
        this.shoppingCarts = shoppingCarts;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPassword() {
        return password;
    }
}
