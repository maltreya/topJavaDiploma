package com.topjava.restaurantvoiting.util;

import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.to.RestaurantTo;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RestaurantUtil {
    public static List<RestaurantTo> getTos(Collection<Restaurant> restaurants){
        return restaurants.stream().map(RestaurantUtil::createTo).toList();
    }
    public static RestaurantTo createTo(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(),restaurant.getName());
    }

    public static List<RestaurantTo> getTosWithMenu(Collection<Restaurant> restaurants){
        return restaurants.stream().map(RestaurantUtil::createToWithMenu).toList();
    }

    public static RestaurantTo createToWithMenu(Restaurant restaurant) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(),restaurant.getFoods());
    }
}
