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
public class ShoppingCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SneakerStoreUserDetailService sneakerStoreUserDetailService;

    @Test
    public void addProductToTheShoppingCartShouldReturnBadRequestStatusBecauseOfEmptyBody() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("ProductId", "");
        body.put("Size", "");

       JSONObject object = new JSONObject(body);
       mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart").header("Authorization", "Bearer "+token)
               .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
               .andExpect(status().isBadRequest())
                 .andReturn();
    }

    @Test
    public void addProductToTheShoppingCartShouldReturnNotFoundStatusBecauseProductIsNoAvailable() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("ProductId", "99");
        body.put("Size", "40");
        body.put("UserId", "9");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void addProductToTheShoppingCartShouldReturnNotFoundStatusBecauseProductIsAvailableButChosenSizeIsNotAvailable() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("ProductId", "1");
        body.put("Size", "47 1/3");
        body.put("UserId", "9");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
