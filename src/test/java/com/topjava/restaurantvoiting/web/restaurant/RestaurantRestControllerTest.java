package com.topjava.restaurantvoiting.web.restaurant;

import com.topjava.restaurantvoiting.service.RestaurantService;
import com.topjava.restaurantvoiting.web.vote.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.restaurantvoiting.UtilTest.userHttpBasic;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.RESTAURANT1_ID;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.RESTAURANT_MATCHER;
import static com.topjava.restaurantvoiting.data.UserDataTest.user1;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
public class RestaurantRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/restaurants";
    @Autowired
    private RestaurantService restaurantService;

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getMenuOfDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/'+RESTAURANT1_ID + "/menu")
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantService.getMenuOfDay(RESTAURANT1_ID)));
    }
    @Test
    void getAllWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"/with-menu")
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantService.getAllWithMenu()));
    }
}
