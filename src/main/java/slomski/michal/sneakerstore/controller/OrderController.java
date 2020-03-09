package slomski.michal.sneakerstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import slomski.michal.sneakerstore.model.*;
import slomski.michal.sneakerstore.repositories.AddressRepository;
import slomski.michal.sneakerstore.repositories.PurchaseRepository;
import slomski.michal.sneakerstore.repositories.ShoppingCartRepository;
import slomski.michal.sneakerstore.repositories.UserRepository;
import slomski.michal.sneakerstore.security.AuthenticationSystem;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

@RestController
public class OrderController {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;


    @PostMapping("/order")
    public ResponseEntity<?> placeAnOrder(@RequestBody Map<String,String> body) throws Exception {

        if(body.get("DeliveryAddressId").isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Please choose your delivery address!"),HttpStatus.BAD_REQUEST);

        String deliveryAddressId = body.get("DeliveryAddressId");
        Long userId = AuthenticationSystem.getLoggedInUserId();
        Optional<User> user = userRepository.findById(userId);
        Optional<ShoppingCart> shoppingCart=shoppingCartRepository.findByUser_UserId(userId);
        Optional<Address> deliveryAddress = addressRepository.findById(Long.parseLong(deliveryAddressId));
        if(user.isPresent() && shoppingCart.isPresent() && deliveryAddress.isPresent()){
            Purchase purchase = new Purchase(user.get(),deliveryAddress.get(),new HashSet<>(shoppingCart.get().getShoppingCartProducts()));
            for(Product p: shoppingCart.get().getShoppingCartProducts())
                purchase.addProduct(p);
            return ResponseEntity.ok(purchaseRepository.save(purchase));
        }
        else
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.NOT_FOUND, "There is no such delivery address or shopping cart!"), HttpStatus.NOT_FOUND);




    }


}
