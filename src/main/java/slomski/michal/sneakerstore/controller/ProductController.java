package slomski.michal.sneakerstore.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import slomski.michal.sneakerstore.model.*;
import slomski.michal.sneakerstore.repositories.*;

import java.util.*;


@RestController
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    RoleRepository roleRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping("/products")
    public ResponseEntity<?> addNewProduct(@RequestBody Map<String, ArrayList<String>> body) throws Exception {

        if (body.get("ProductName").get(0).isEmpty() || body.get("BrandName").get(0).isEmpty() || body.get("ProductPrice").get(0).isEmpty() || body.get("Gender").get(0).isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST, "Missing data during adding new product to the store!"), HttpStatus.BAD_REQUEST);
        else {

            String productName = body.get("ProductName").get(0);
            String brandName = body.get("BrandName").get(0);
            String productPrice = body.get("ProductPrice").get(0);
            String gender = body.get("Gender").get(0);
            ArrayList<String> sizes = body.get("Sizes");
            String categoryName = body.get("Category").get(0);

            Brand brand = brandRepository.findByBrandName(BrandName.valueOf(brandName)).orElseThrow(() -> new Exception("Brand name not set!"));
            Set<Category> categories = new HashSet<>();
            Category category = categoryRepository.findByCategoryName(CategoryName.valueOf(categoryName)).orElseThrow(() -> new Exception("Brand name not set!"));
            categories.add(category);
            Product product = new Product(productName, brand, productPrice, sizes, Gender.valueOf(gender), categories);
            return ResponseEntity.ok().body(productRepository.save(product));
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/products/{productId}")
    public ResponseEntity<?> editExistingProduct(@PathVariable("productId") Long productId, @RequestBody Map<String, ArrayList<String>> body) throws Exception {

        if (body.get("ProductName").isEmpty() || body.get("BrandName").isEmpty() || body.get("ProductPrice").isEmpty() || body.get("Gender").isEmpty())
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Missing data during adding new product to the store!"), HttpStatus.BAD_REQUEST);
        else if (productRepository.findById(productId).isPresent()) {

            String updatedProductName = body.get("ProductName").get(0);
            String updatedBrandName = body.get("BrandName").get(0);
            String updatedProductPrice = body.get("ProductPrice").get(0);
            String updatedGender = body.get("Gender").get(0);
            ArrayList<String> updatedSizes = body.get("Sizes");
            String updatedCategoryName = body.get("Category").get(0);

            Product updatedProduct;
            updatedProduct = productRepository.findById(productId).get();
            updatedProduct.setProductName(updatedProductName);
            updatedProduct.setProductPrice(updatedProductPrice);
            updatedProduct.setSizes(updatedSizes);
            return ResponseEntity.ok().body(productRepository.save(updatedProduct));
        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST, "Product not found!"), HttpStatus.BAD_REQUEST);

    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity deleteProductWithGivenId(@PathVariable("productId") Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            productRepository.delete(product);
            return new ResponseEntity<>(new ApiResponse(true, HttpStatus.OK,
                    "Product with id number: " + productId + " has been deleted successfully!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Product with id number: " + productId + " does not exist!"), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {

        if (productRepository.findById(productId).isPresent()) {
            Product product = productRepository.findById(productId).get();
            return ResponseEntity.ok().body(product);
        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,
                    "Product with given ID: " + productId + " does not exist!"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/{gender}/show-all")
    public ResponseEntity<?> getAllProductsByGender(@PathVariable("gender") String gender) {

        if (gender.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Missing data during showing products by gender!"), HttpStatus.BAD_REQUEST);
        }

        if (!productRepository.findAllByGender(Gender.valueOf(gender.toUpperCase())).isEmpty()) {
            return ResponseEntity.ok().body(productRepository.findAllByGender(Gender.valueOf(gender.toUpperCase())));

        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"There is no " + gender + " products in the store!"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products/{brandName}/all")
    public ResponseEntity<?> getAllProductsByBrandName(@PathVariable("brandName") String brandName) {

        if (brandName.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Missing data during showing products by brand name!"), HttpStatus.BAD_REQUEST);
        }
        Optional<Brand> brand = brandRepository.findByBrandName(BrandName.valueOf(brandName.toUpperCase()));
        if (brand.isPresent() && !productRepository.findAllByBrand(brand.get()).isEmpty()) {

            return ResponseEntity.ok().body(productRepository.findAllByBrand(brand.get()));

        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"There is no " + brandName + " products in the store!"), HttpStatus.NOT_FOUND);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok().body(productRepository.findAll());
    }

    @GetMapping("/products/{brandName}/{gender}")
    public ResponseEntity<?> getAllProductsByBrandAndGender(@PathVariable("brandName") String brandName, @PathVariable("gender") String gender) {

        if (brandName.isEmpty() || gender.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.BAD_REQUEST,"Missing data during showing products by brand and gender!"), HttpStatus.BAD_REQUEST);
        }

        Optional<Brand> brand = brandRepository.findByBrandName(BrandName.valueOf(brandName.toUpperCase()));

        if (brand.isPresent() && !productRepository.findAllByGender(Gender.valueOf(gender.toUpperCase())).isEmpty() &&!productRepository.findAllByBrand(brand.get()).isEmpty()) {
            return ResponseEntity.ok().body(productRepository.findAllByBrandAndGender(brand.get(),Gender.valueOf(gender.toUpperCase())));

        } else
            return new ResponseEntity<>(new ApiResponse(false, HttpStatus.NOT_FOUND,"There is no " + gender + " " + brandName + " products in the store!"), HttpStatus.NOT_FOUND);
    }


}
