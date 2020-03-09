package slomski.michal.sneakerstore;


import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import slomski.michal.sneakerstore.security.JwtUtil;
import slomski.michal.sneakerstore.security.SneakerStoreUserDetailService;

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SneakerStore.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SneakerStoreUserDetailService sneakerStoreUserDetailService;


    @Test
    public void addNewProductToTheStoreByADMINShouldReturnBadRequestBecauseNoData() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("admin@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, ArrayList<String>> body = new HashMap<>();

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("");
        ArrayList<String> brandNameList = new ArrayList<>();
        brandNameList.add("NIKE");
        ArrayList<String> productPriceList = new ArrayList<>();
        productPriceList.add("");
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("MALE");
        ArrayList<String> sizesList = new ArrayList<>();
        sizesList.add("");
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("LIFESTYLE");

        body.put("ProductName", productNameList);
        body.put("BrandName", brandNameList);
        body.put("ProductPrice", productPriceList);
        body.put("Gender", genderList);
        body.put("Sizes", sizesList);
        body.put("Category", categoryList);

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/products").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void addNewProductToTheStoreByUSERShouldReturnForbiddenStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, ArrayList<String>> body = new HashMap<>();

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("yeezy boost 350 v2");
        ArrayList<String> brandNameList = new ArrayList<>();
        brandNameList.add("ADIDAS");
        ArrayList<String> productPriceList = new ArrayList<>();
        productPriceList.add("1099");
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("MALE");
        ArrayList<String> sizesList = new ArrayList<>();
        sizesList.add("");
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("LIFESTYLE");

        body.put("ProductName", productNameList);
        body.put("BrandName", brandNameList);
        body.put("ProductPrice", productPriceList);
        body.put("Gender", genderList);
        body.put("Sizes", sizesList);
        body.put("Category", categoryList);

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/products").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void editProductByUSERShouldReturnForbiddenStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, ArrayList<String>> body = new HashMap<>();

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("yeezy boost 350 v2");
        ArrayList<String> brandNameList = new ArrayList<>();
        brandNameList.add("ADIDAS");
        ArrayList<String> productPriceList = new ArrayList<>();
        productPriceList.add("1099");
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("MALE");
        ArrayList<String> sizesList = new ArrayList<>();
        sizesList.add("");
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("LIFESTYLE");

        body.put("ProductName", productNameList);
        body.put("BrandName", brandNameList);
        body.put("ProductPrice", productPriceList);
        body.put("Gender", genderList);
        body.put("Sizes", sizesList);
        body.put("Category", categoryList);

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    public void editExistingProductShouldReturnBadRequestBecauseOfEmptyData() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("admin@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, ArrayList<String>> body = new HashMap<>();

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("");
        ArrayList<String> brandNameList = new ArrayList<>();
        brandNameList.add("");
        ArrayList<String> productPriceList = new ArrayList<>();
        productPriceList.add("");
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("");
        ArrayList<String> sizesList = new ArrayList<>();
        sizesList.add("");
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("");

        body.put("ProductName", productNameList);
        body.put("BrandName", brandNameList);
        body.put("ProductPrice", productPriceList);
        body.put("Gender", genderList);
        body.put("Sizes", sizesList);
        body.put("Category", categoryList);

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/1").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }



    @Test
    public void editExistingProductShouldReturnBadRequestBecauseThereIsNoSuchProductInTheStore() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("admin@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, ArrayList<String>> body = new HashMap<>();

        ArrayList<String> productNameList = new ArrayList<>();
        productNameList.add("yeezy boost 350 v2");
        ArrayList<String> brandNameList = new ArrayList<>();
        brandNameList.add("ADIDAS");
        ArrayList<String> productPriceList = new ArrayList<>();
        productPriceList.add("1099");
        ArrayList<String> genderList = new ArrayList<>();
        genderList.add("MALE");
        ArrayList<String> sizesList = new ArrayList<>();
        sizesList.add("");
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("LIFESTYLE");


        body.put("ProductName", productNameList);
        body.put("BrandName", brandNameList);
        body.put("ProductPrice", productPriceList);
        body.put("Gender", genderList);
        body.put("Sizes", sizesList);
        body.put("Category", categoryList);

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/products/9999").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteProductShouldReturnBadRequestBecauseThereIsNoSuchProductInTheStore() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("admin@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/9999").header("Authorization", "Bearer "+token))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteProductShouldReturnForbiddenStatusBecauseUserIsTryingToDoIt() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.delete("/products/1").header("Authorization", "Bearer "+token))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @Test
    public void getAllProductsTestShouldReturnOkStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getProductsByIDTestShouldReturnOkStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/3"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getProductsByIDWhichDoesNotExistInDatabaseTestShouldReturnNotFoundStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/9999"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getProductsByGenderTestShouldReturnOkStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/male/show-all"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getProductsByGenderWithNoFemaleProductsInTheDatabaseTestShouldReturnNotFoundStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/female/show-all"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getProductsByBrandNameTestShouldReturnOkStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/adidas/all"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getProductsByBrandNameWhichNotExistInTheStoreTestShouldReturnNotFoundStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/puma/all"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getProductsByBrandNameAndGenderTestShouldReturnOkStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/adidas/male"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getProductsByBrandNameAndGenderWhichNotExistInTheStoreTestShouldReturnNotFoundStatus() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/products/puma/male"))
                .andExpect(status().isNotFound())
                .andReturn();
    }



}
