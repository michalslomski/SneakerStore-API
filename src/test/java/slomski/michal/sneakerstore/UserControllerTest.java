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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = SneakerStore.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SneakerStoreUserDetailService sneakerStoreUserDetailService;

    @Test
    public void signUpTestWithDifferentPasswordsShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-up").header("EmailAddress", "michal@gmail.com").header("Password", "zaq1@WSX")
                .header("FirstName", "Michal").header("LastName", "Slomski").header("Password", "12345").header("ConfirmPassword", "1234"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestWithExistingUserShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-up").header("EmailAddress", "realadmin@gmail.com").header("Password", "zaq1@WSX")
                .header("FirstName", "Michal").header("LastName", "Slomski").header("Password", "12345").header("ConfirmPassword", "1234"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signUpTestWithTooShortEmailAddressShouldReturnBadRequestStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-up").header("EmailAddress", "")
                .header("FirstName", "Michal").header("LastName", "Slomski").header("Password", "12345").header("ConfirmPassword", "12345"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signInTestWithEmptyEmailAddressAndPasswordShouldReturnBadRequestStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("EmailAddress", "");
        body.put("Password", "");


        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-in").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signInTestWithEmptyEmailAddressShouldReturnBadRequestStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("EmailAddress", "");
        body.put("Password", "asdasd");


        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-in").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void signInTestWithEmptyPasswordShouldReturnBadRequestStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("EmailAddress", "user@gmail.com");
        body.put("Password", "");


        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-in").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    public void signInTestShouldReturnOkStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("EmailAddress", "user@gmail.com");
        body.put("Password", "123456");


        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-in").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void signInTestShouldReturnForbiddenStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("EmailAddress", "master@gmail.com");
        body.put("Password", "dasdas");


        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/sign-in").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @Test
    public void getUserDataWithGivenIdShouldReturnOkStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/9/data")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUserDataAnotherThanLoggedInShouldReturnUnauthorizedStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/10/data")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void addNewAddressTestShouldReturnBadRequestStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();
        body.put("UserId", "9999");
        body.put("AddressName", "");
        body.put("FirstName", "");
        body.put("LastName", "");
        body.put("StreetAddress", "");
        body.put("HomeNumber", "");
        body.put("PostalCode", "");
        body.put("City", "");
        body.put("Country", "");
        body.put("PhoneNumber", "");
        body.put("EmailAddress", "");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/users/address").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editExistingAddressTestShouldReturnUnauthorizedStatusBecause() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("AddressName", "");
        body.put("FirstName", "");
        body.put("LastName", "");
        body.put("StreetAddress", "");
        body.put("HomeNumber", "");
        body.put("PostalCode", "");
        body.put("City", "");
        body.put("Country", "");
        body.put("PhoneNumber", "");
        body.put("EmailAddress", "");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/8/address/1").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void editExistingAddressTestWithMissingDataShouldReturnBadRequestStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("AddressName", "");
        body.put("FirstName", "");
        body.put("LastName", "");
        body.put("StreetAddress", "");
        body.put("HomeNumber", "60");
        body.put("PostalCode", "");
        body.put("City", "");
        body.put("Country", "");
        body.put("PhoneNumber", "");
        body.put("EmailAddress", "");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/9/address/1").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void editExistingAddressTestShouldReturnNotFoundStatusBecauseUserHaveNoAddressWithGivenId() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("AddressName", "das");
        body.put("FirstName", "das");
        body.put("LastName", "das");
        body.put("StreetAddress", "sad");
        body.put("HomeNumber", "60");
        body.put("PostalCode", "00-000");
        body.put("City", "dsa");
        body.put("Country", "dasd");
        body.put("PhoneNumber", "134567892");
        body.put("EmailAddress", "ldpas@gmail.com");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.put("/users/9/address/100").header("Authorization",
                "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteExistingAddressShouldReturnUnauthorizedStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/8/address/100").header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void deleteExistingAddressShouldReturnNotFoundStatus() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/9/address/100").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getUserOrdersShouldReturnOkStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/9/orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getUserOrdersShouldReturnNotFoundStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("userWithoutOrders98@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/11/orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void getUserOrdersShouldReturnUnauthorizedStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/100/orders")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    public void getUserShoppingCartShouldReturnUnauthorizedStatus() throws Exception {

        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/100/shopping-cart")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }


}
