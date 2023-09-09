package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.data.FoodDataTest;
import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


import static com.topjava.restaurantvoiting.data.FoodDataTest.FOOD_MATCHER;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestaurantServiceTest extends AbstractServiceTest {
    @Autowired
    protected RestaurantService restaurantService;

    @Test
    void create() {
        Restaurant created = restaurantService.create(getNew());
        int newId = created.id();
        Restaurant newRestaurant = getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(newId), newRestaurant);
    }

    @Test
    void delete() {
        restaurantService.delete(RESTAURANT1_ID);
        assertThrows(NotFoundException.class,
                () -> restaurantService.get(RESTAURANT1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> restaurantService.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Restaurant restaurant = restaurantService.get(RESTAURANT1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> restaurantService.get(NOT_FOUND));
    }

    @Test
    void update() {
        Restaurant updated = getUpdated();
        restaurantService.update(updated);
        RESTAURANT_MATCHER.assertMatch(restaurantService.get(RESTAURANT1_ID), getUpdated());
    }

    @Test
    void getAll() {
        RESTAURANT_MATCHER.assertMatch(restaurantService.getAll(), restaurants);
    }

    @Test
    void getMenuOfDay() {
        List<Food> restaurantMenuOfDay = restaurantService.getMenuOfDay(RESTAURANT3_ID).getFoods();
        FOOD_MATCHER.assertMatch(restaurantMenuOfDay, FoodDataTest.menuOfDay);
    }

    @Test
    void createdWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class,
                () -> restaurantService.create(new Restaurant(null, " "))
        );

    }

}
