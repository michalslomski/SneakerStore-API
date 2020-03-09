package slomski.michal.sneakerstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slomski.michal.sneakerstore.model.*;
import slomski.michal.sneakerstore.repositories.ProductRepository;
import slomski.michal.sneakerstore.repositories.ShoppingCartRepository;
import slomski.michal.sneakerstore.repositories.UserRepository;
import slomski.michal.sneakerstore.security.AuthenticationSystem;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
public class ShoppingCartController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/shopping-cart")
    public ResponseEntity<?> addProductToTheShoppingCart(@RequestBody Map<String, String> body) throws Exception {

        if (body.get("ProductId").isEmpty() || body.get("Size").isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST, "Missing data during adding product to the cart!"), HttpStatus.BAD_REQUEST);

        if (!AuthenticationSystem.getLoggedInUserId().equals(Long.parseLong(body.get("UserId"))))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to do this!"), HttpStatus.UNAUTHORIZED);

        if (productRepository.findById(Long.parseLong(body.get("ProductId"))).isPresent()) {

            Product product = productRepository.findById(Long.parseLong(body.get("ProductId"))).get();

            if (product.getSizes().contains(body.get("Size"))) {
                Long userId = AuthenticationSystem.getLoggedInUserId();
                Optional<User> user = userRepository.findById(userId);
                if (user.isPresent()) {
                    Optional<ShoppingCart> shoppingCartOptional = shoppingCartRepository.findByUser_UserId(userId);
                    if (shoppingCartOptional.isPresent()) {
                        ShoppingCart shoppingCart = shoppingCartOptional.get();
                        Set<Product> products = shoppingCart.getShoppingCartProducts();
                        products.add(product);
                        shoppingCart.setShoppingCartProducts(products);
                        return ResponseEntity.ok().body(shoppingCartRepository.save(shoppingCart));

                    } else {
                        ShoppingCart shoppingCart = new ShoppingCart(user.get());
                        shoppingCart.setShoppingCartProducts(Collections.singleton(product));
                        return ResponseEntity.ok().body(shoppingCartRepository.save(shoppingCart));
                    }

                } else
                    return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"There is no such user"), HttpStatus.NOT_FOUND);


            } else
                return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"Size is not available!"), HttpStatus.NOT_FOUND);


        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"Product with given ID is no longer available!"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/shopping-cart/{id}")
    public ResponseEntity<?> deleteProductFromTheShoppingCart(@RequestBody Map<String, String> body, @PathVariable("id") Long shoppingCartId) throws Exception {


        if (body.get("ProductId").isEmpty() || body.get("UserId").isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST, "Missing data during editing your shopping cart!"), HttpStatus.BAD_REQUEST);

        if (!AuthenticationSystem.getLoggedInUserId().equals(Long.parseLong(body.get("UserId"))))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to do this!"), HttpStatus.UNAUTHORIZED);
        if (shoppingCartRepository.findById(shoppingCartId).isPresent()) {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
            if (shoppingCart.isPresent()) {
                Long productId = Long.parseLong(body.get("ProductId"));
                ShoppingCart s = shoppingCart.get();
                Set<Product> productsInTheShoppingCart = s.getShoppingCartProducts();
                for (Product product : productsInTheShoppingCart) {
                    if (product.getProductId().equals(productId)) {
                        productsInTheShoppingCart.remove(product);
                    }
                }
                s.setShoppingCartProducts(productsInTheShoppingCart);
                return ResponseEntity.ok(shoppingCartRepository.save(s));
            } else
                return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"Given user have no such shopping cart!!"), HttpStatus.NOT_FOUND);


        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"Given user have no such shopping cart!!"), HttpStatus.NOT_FOUND);
    }
}


