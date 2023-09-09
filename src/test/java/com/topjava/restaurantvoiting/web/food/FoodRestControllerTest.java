package com.topjava.restaurantvoiting.web.food;

import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.service.FoodService;
import com.topjava.restaurantvoiting.to.FoodTo;
import com.topjava.restaurantvoiting.util.FoodUtil;
import com.topjava.restaurantvoiting.util.JsonUtil;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import com.topjava.restaurantvoiting.web.vote.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.UtilTest.userHttpBasic;
import static com.topjava.restaurantvoiting.data.FoodDataTest.*;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.RESTAURANT1_ID;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.RESTAURANT4_ID;
import static com.topjava.restaurantvoiting.data.UserDataTest.admin;
import static com.topjava.restaurantvoiting.data.UserDataTest.user1;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class FoodRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/admin/foods";
    @Autowired
    private FoodService foodService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+'/'+FOOD1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FOOD_TO_MATCHER.contentJson(FoodUtil.createTo(food1)));
    }
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FOOD_TO_MATCHER.contentJson(FoodUtil.getTos(foods)));
    }
    @Test
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+"/restaurant?restaurantId="+RESTAURANT1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FOOD_TO_MATCHER.contentJson(FoodUtil.getTos(foodsByRestaurant)));
    }
    @Test
    void getAllByDate() throws Exception {
        List<FoodTo> expected = FoodUtil.getTos(foodService.getAllByDate(LocalDate.of(2023,7,7)));
        perform(MockMvcRequestBuilders.get(REST_URL+"/by-date?prepDate="+ LocalDate.of(2023,7,7))
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FOOD_TO_MATCHER.contentJson(expected));
    }
    @Test
    void getAllByRestaurantAndDate() throws Exception {
        List<FoodTo> expected=FoodUtil.getTos(foodService.getAllByRestaurantAndDate(RESTAURANT4_ID,
                LocalDate.of(2023,7,7)));
        perform(MockMvcRequestBuilders.get(REST_URL+
                "/by-restaurant-date?restaurantId="+RESTAURANT4_ID+"&prepDate="+LocalDate.of(2023,7,7))
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(FOOD_TO_MATCHER.contentJson(expected));
    }
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+'/'+FOOD1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class,
                () -> foodService.get(RESTAURANT1_ID));
    }
    @Test
    void getUnauthorized() throws Exception {
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
        Food newFood = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL
        +"/restaurant?restaurantId="+RESTAURANT1_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newFood)))
                .andExpect(status().isCreated());

        FoodTo created = FOOD_TO_MATCHER.readFromJson(action);
        int newId = created.id();;
        newFood.setId(newId);
        FOOD_TO_MATCHER.assertMatch(created,FoodUtil.createTo(newFood));
        FOOD_MATCHER.assertMatch(foodService.get(newId),newFood);
    }
    @Test
    void update() throws Exception {
        Food updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL+
                "/restaurant?restaurantId="+RESTAURANT1_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        FOOD_MATCHER.assertMatch(foodService.get(FOOD1_ID),updated);
    }
}
