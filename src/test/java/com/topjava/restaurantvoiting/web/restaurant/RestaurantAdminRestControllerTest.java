package com.topjava.restaurantvoiting.web.restaurant;

import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.service.RestaurantService;
import com.topjava.restaurantvoiting.util.JsonUtil;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import com.topjava.restaurantvoiting.web.vote.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.topjava.restaurantvoiting.UtilTest.userHttpBasic;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.*;
import static com.topjava.restaurantvoiting.data.UserDataTest.admin;
import static com.topjava.restaurantvoiting.data.UserDataTest.user1;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RestaurantAdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/admin/restaurants";
    @Autowired
    private RestaurantService restaurantService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantService.getAllWithMenu()));
    }

    @Test
    void getAllWithMenu() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/with-menu")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantService.getAllWithMenu()));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class,
                () -> restaurantService.get(RESTAURANT1_ID));
    }

    @Test
    void getMenuOfDay() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + RESTAURANT1_ID + "/menu")
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurantService.getMenuOfDay(RESTAURANT1_ID)));
    }

    @Test
    void getUnauthorised() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }
    @Test
    void createLocation() throws Exception {
        Restaurant newRestaurant = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isCreated());

        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        int newId = created.id();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created,newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId),newRestaurant);
    }
    @Test
    void update() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT1_ID),updated);
    }

}
