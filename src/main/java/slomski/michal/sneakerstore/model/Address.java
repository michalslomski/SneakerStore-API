package slomski.michal.sneakerstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotNull
    private String addressName;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String streetAddress;

    @NotNull
    private String homeNumber;

    @NotNull
    private String postalCode;

    @NotNull
    private String city;

    @NotNull
    private String country;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    @ManyToOne
    @JsonBackReference
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<Purchase> purchases = new HashSet<>();

    public Address(@NotNull String addressName,@NotNull String firstName, @NotNull String lastName, @NotNull String streetAddress, @NotNull String homeNumber, @NotNull String postalCode, @NotNull String city, @NotNull String country, @NotNull String phoneNumber, @NotNull String email, User user) {
        this.addressName = addressName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.homeNumber = homeNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.user = user;
    }

    public Address() {
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Set<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }
}