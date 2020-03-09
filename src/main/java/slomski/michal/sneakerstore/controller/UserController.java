package slomski.michal.sneakerstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import slomski.michal.sneakerstore.model.*;
import slomski.michal.sneakerstore.repositories.AddressRepository;
import slomski.michal.sneakerstore.repositories.RoleRepository;
import slomski.michal.sneakerstore.repositories.UserRepository;

import slomski.michal.sneakerstore.security.AuthenticationSystem;
import slomski.michal.sneakerstore.security.JwtUtil;
import slomski.michal.sneakerstore.security.SneakerStoreUserDetailService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SneakerStoreUserDetailService sneakerStoreUserDetailService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    RoleRepository roleRepository;

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerNewUser(@RequestHeader("FirstName") String firstName, @RequestHeader("LastName") String lastName, @RequestHeader("EmailAddress") String emailAddress, @RequestHeader("Password") String password, @RequestHeader("ConfirmPassword") String confirmPassword) throws Exception {


        if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Missing data during registration process!"), HttpStatus.BAD_REQUEST);
        if (!password.equals(confirmPassword))
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.BAD_REQUEST, "Those passwords didn't match. Try again!"), HttpStatus.BAD_REQUEST);
        if (userRepository.existsByEmailAddress(emailAddress)) {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"User with this email address already exists!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User(emailAddress, password, firstName, lastName, new ArrayList<>());
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new Exception("User role not set!"));
        user.setRoles(Collections.singleton(userRole));
        userRepository.save(user);
        return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK, "User registered successfully"), HttpStatus.OK);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody Map<String, String> body ) {

        if (body.get("EmailAddress").isEmpty() || body.get("Password").isEmpty()) {
            return new ResponseEntity<>("Missing data during login process!", HttpStatus.BAD_REQUEST);

        } else {
            String emailAddress = body.get("EmailAddress");
            String password = body.get("Password");
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(emailAddress, password));
            } catch (BadCredentialsException e) {
                return new ResponseEntity<>("Unauthorized!", HttpStatus.UNAUTHORIZED);

            }

            final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername(emailAddress);

            final String jwt = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        }
    }

    @GetMapping("/{id}/data")
    public ResponseEntity<?> getUserData(@PathVariable("id") Long userId) {

       if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
           return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to view this content!"), HttpStatus.UNAUTHORIZED);
        User user;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
            return ResponseEntity.ok().body(user);
        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"User with ID: " + userId + " not found!"), HttpStatus.NOT_FOUND);
    }

    @PostMapping("/address")
    public ResponseEntity addNewAddress(@RequestBody Map<String, String> body) {


        if (body.get("UserId").isEmpty() || body.get("AddressName").isEmpty() || body.get("FirstName").isEmpty() || body.get("LastName").isEmpty() || body.get("StreetAddress").isEmpty() ||  body.get("HomeNumber").isEmpty() || body.get("PostalCode").isEmpty() ||  body.get("City").isEmpty() || body.get("Country").isEmpty() ||  body.get("PhoneNumber").isEmpty() || body.get("EmailAddress").isEmpty())
            return new ResponseEntity<>( new ApiResponse(false,HttpStatus.BAD_REQUEST,"Missing data during adding new address!"), HttpStatus.BAD_REQUEST);

        if (!AuthenticationSystem.getLoggedInUserId().equals(Long.parseLong(body.get("UserId"))))
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.UNAUTHORIZED, "You have no permission to view this content!"), HttpStatus.UNAUTHORIZED);

        Optional<User> user = userRepository.findById(Long.parseLong(body.get("UserId")));
        if (user.isPresent()) {
            Address address = new Address(body.get("AddressName"),body.get("FirstName"), body.get("LastName"), body.get("StreetAddress"), body.get("HomeNumber"), body.get("PostalCode"), body.get("City"), body.get("Country"), body.get("PhoneNumber"), body.get("EmailAddress"), user.get());
            return ResponseEntity.ok(addressRepository.save(address));
        } else {
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.BAD_REQUEST, "User is not present!"), HttpStatus.BAD_REQUEST);
        }

    }
    @GetMapping("/{id}/addresses")
    public ResponseEntity<?> getUserAddresses(@PathVariable("id") Long userId) {

        if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to view this content!"), HttpStatus.UNAUTHORIZED);
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(user.get().getAddresses());
        } else
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.NOT_FOUND ,"User with ID: " + userId + " not found!"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{userId}/address/{addressId}")
    public ResponseEntity updateAddress(@PathVariable("userId") Long userId, @PathVariable("addressId") Long addressId, @RequestBody Map<String, String> body) {

        if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to do this!"), HttpStatus.UNAUTHORIZED);

        if (body.get("AddressName").isEmpty()||body.get("FirstName").isEmpty() || body.get("LastName").isEmpty() || body.get("StreetAddress").isEmpty() || body.get("HomeNumber").isEmpty() || body.get("PostalCode").isEmpty() || body.get("City").isEmpty() || body.get("Country").isEmpty() || body.get("PhoneNumber").isEmpty() || body.get("EmailAddress").isEmpty())
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.BAD_REQUEST, "Missing data!"), HttpStatus.BAD_REQUEST);
        if (userRepository.findById(userId).isPresent() && addressRepository.findById(addressId).isPresent()) {
            String updatedAddressName = body.get("AddressName");
            String updatedFirstName = body.get("FirstName");
            String updatedLastName = body.get("LastName");
            String updatedStreetAddress = body.get("StreetAddress");
            String updatedHomeNumber = body.get("HomeNumber");
            String updatedPostalCode = body.get("PostalCode");
            String updatedCityName = body.get("City");
            String updatedCountryName = body.get("Country");
            String updatedPhoneNumber = body.get("PhoneNumber");
            String updatedEmailAddress = body.get("EmailAddress");

            Address address;
            address = addressRepository.findById(addressId).get();
            address.setAddressName(updatedAddressName);
            address.setFirstName(updatedFirstName);
            address.setLastName(updatedLastName);
            address.setStreetAddress(updatedStreetAddress);
            address.setHomeNumber(updatedHomeNumber);
            address.setPostalCode(updatedPostalCode);
            address.setCity(updatedCityName);
            address.setCountry(updatedCountryName);
            address.setPhoneNumber(updatedPhoneNumber);
            address.setEmail(updatedEmailAddress);
            return ResponseEntity.ok(addressRepository.save(address));
        } else {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND, "Check user and address id!"), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}/address/{addressId}")
    public ResponseEntity deleteAddress(@PathVariable("userId") Long userId, @PathVariable("addressId") Long addressId) {

        if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to do this!"), HttpStatus.UNAUTHORIZED);
        if (userRepository.findById(userId).isPresent() && addressRepository.findById(addressId).isPresent()) {
            Address address = addressRepository.findById(addressId).get();
            addressRepository.delete(address);
            return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK,
                    "Address with id number: " + addressId + " has been deleted successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND, "Check user and address id!"), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<?> getUserOrders(@PathVariable("id") Long userId) {

        if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.UNAUTHORIZED, "You have no permission to view this content!"), HttpStatus.UNAUTHORIZED);
        User user;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
            if (!user.getPurchases().isEmpty())
                return ResponseEntity.ok().body(user.getPurchases());
            else
                return new ResponseEntity<>(new ApiResponse(true, HttpStatus.NOT_FOUND,"You have no placed orders yet!"), HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(new ApiResponse(false,HttpStatus.NOT_FOUND, "User with ID: " + userId + " not found!"), HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{id}/shopping-cart")
    public ResponseEntity<?> getUserShoppingCart(@PathVariable("id") Long userId){

        if (!AuthenticationSystem.getLoggedInUserId().equals(userId))
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.UNAUTHORIZED,"You have no permission to view this content!"), HttpStatus.UNAUTHORIZED);

        User user;
        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
            if (!user.getShoppingCarts().isEmpty())
                return ResponseEntity.ok().body(user.getShoppingCarts());
            else
                return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK,"Your shopping cart is empty!"), HttpStatus.OK);
        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"User with ID: " + userId + " not found!"), HttpStatus.NOT_FOUND);
    }
}

