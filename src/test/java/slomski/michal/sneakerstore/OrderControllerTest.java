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
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SneakerStoreUserDetailService sneakerStoreUserDetailService;

    @Test
    public void placeAnOrderShouldReturnBadRequestStatusBecauseBodyIsEmpty() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("DeliveryAddressId", "");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/order").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void placeAnOrderShouldReturnNotFoundStatusBecauseDeliveryAddressIsNotValid() throws Exception {
        final UserDetails userDetails = sneakerStoreUserDetailService.loadUserByUsername("user@gmail.com");
        final String token = jwtUtil.generateToken(userDetails);

        Map<String, String> body = new HashMap<>();

        body.put("DeliveryAddressId", "99");

        JSONObject object = new JSONObject(body);
        mockMvc.perform(MockMvcRequestBuilders.post("/order").header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(object.toString()))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
