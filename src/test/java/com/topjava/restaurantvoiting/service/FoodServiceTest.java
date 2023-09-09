package com.topjava.restaurantvoiting.service;


import com.topjava.restaurantvoiting.data.FoodDataTest;
import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.data.FoodDataTest.*;
import static com.topjava.restaurantvoiting.data.FoodDataTest.NOT_FOUND;
import static com.topjava.restaurantvoiting.data.RestaurantDataTest.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodServiceTest extends AbstractServiceTest {
    @Autowired
    protected FoodService foodService;

    @Test
    void create() {
        Food created = foodService.create(FoodDataTest.getNew(), RESTAURANT1_ID);
        int newId = created.id();
        Food newFood = FoodDataTest.getNew();
        newFood.setId(newId);
        FOOD_MATCHER.assertMatch(created, newFood);
        FOOD_MATCHER.assertMatch(foodService.get(newId), newFood);
    }

    @Test
    void duplicateFood() {
        assertThrows(DataAccessException.class,
                () -> foodService.create(new Food(null, LocalDate.now(), food1.getDescription(),
                        new BigDecimal("4.56"), food1.getRestaurant()), RESTAURANT1_ID));
    }

    @Test
    void delete() {
        foodService.delete(FOOD1_ID);
        assertThrows(NotFoundException.class,
                () -> foodService.get(FOOD1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> foodService.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Food food = foodService.get(FOOD1_ID);
        FOOD_MATCHER.assertMatch(food, food1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> foodService.get(NOT_FOUND));
    }

    @Test
    void getAllByDate() {
        List<Food> allFoodByDate = foodService.getAllByDate(LocalDate.of(2023, 7, 7));
        FOOD_MATCHER.assertMatch(allFoodByDate, foodsByDate);
    }

    @Test
    void getAllByRestaurantAndDate() {
        List<Food> allFoodByRestaurantAndDate = foodService.getAllByRestaurantAndDate(RESTAURANT4_ID, LocalDate.of(2023, 7, 7));
        FOOD_MATCHER.assertMatch(allFoodByRestaurantAndDate, foodsByDate);
    }

    @Test
    void update() {
        Food updated = FoodDataTest.getUpdated();
        foodService.update(updated, RESTAURANT1_ID);
        FOOD_MATCHER.assertMatch(foodService.get(FOOD1_ID), FoodDataTest.getUpdated());
    }

    @Test
    void getAll() {
        FOOD_MATCHER.assertMatch(foodService.getAll(), foods);
    }

    @Test
    void getAllByRestaurant() {
        FOOD_MATCHER.assertMatch(foodService.getAllByRestaurant(RESTAURANT1_ID), foodsByRestaurant);
    }

    @Test
    void createWithException() throws Exception{
        validateRootCause(ConstraintViolationException.class,
                () -> foodService.create(new Food(
                        null, null,"NewFood", new BigDecimal("55.55"),restaurant1),
                        RESTAURANT1_ID));

        validateRootCause(ConstraintViolationException.class,
                () -> foodService.create(new Food(
                                null, LocalDate.now()," ", new BigDecimal("55.55"),restaurant1),
                        RESTAURANT1_ID));

        validateRootCause(ConstraintViolationException.class,
                () -> foodService.create(new Food(
                                null, LocalDate.now(),"NewFood", new BigDecimal("-55.55"),restaurant1),
                        RESTAURANT1_ID));

        validateRootCause(ConstraintViolationException.class,
                () -> foodService.create(new Food(
                                null, LocalDate.now(),"NewFood", new BigDecimal("1055.55"),restaurant1),
                        RESTAURANT1_ID));
    }
}

