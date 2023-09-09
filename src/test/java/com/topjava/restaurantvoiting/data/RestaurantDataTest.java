package com.topjava.restaurantvoiting.data;

import com.topjava.restaurantvoiting.MatchFactory;
import com.topjava.restaurantvoiting.model.Restaurant;

import java.util.List;

import static com.topjava.restaurantvoiting.model.AbstractBaseEntity.START_SEQ;

public class RestaurantDataTest {
    public static final MatchFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatchFactory.usingIgnoringFieldsComparator(
            Restaurant.class, "foods", "votes");
    public static final int NOT_FOUND = 11;
    public static final int RESTAURANT1_ID = START_SEQ + 5;
    public static final int RESTAURANT2_ID = START_SEQ + 6;
    public static final int RESTAURANT3_ID = START_SEQ + 7;
    public static final int RESTAURANT4_ID = START_SEQ + 8;
    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID,"Chleb i Wino restaurant");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID,"Mandu restaurant");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID,"Surf Burger restaurant");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID,"Restauracja Fino restaurant");

    public static final List<Restaurant> restaurants = List.of(restaurant1,restaurant2,restaurant3,restaurant4);

    public static Restaurant getNew(){
        return new Restaurant(null,"NewRestaurant restaurant");
    }
    public static Restaurant getUpdated(){
        return new Restaurant(RESTAURANT1_ID,"UpdatedRestaurant restaurant");
    }
}
