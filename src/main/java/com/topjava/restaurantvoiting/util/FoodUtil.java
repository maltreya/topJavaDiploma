package com.topjava.restaurantvoiting.util;

import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.to.FoodTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class FoodUtil {
    public static List<FoodTo> getTos(Collection<Food> foods) {
        return foods.stream().map(FoodUtil::createTo).toList();
    }

    public static FoodTo createTo(Food food) {
        return new FoodTo(food.getId(), food.getPrepDate(),
                food.getDescription(), food.getPrice(),
                food.getRestaurant().getId());
    }
}
