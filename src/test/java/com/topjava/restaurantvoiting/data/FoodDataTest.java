package com.topjava.restaurantvoiting.data;

import com.topjava.restaurantvoiting.MatchFactory;
import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.to.FoodTo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.data.RestaurantDataTest.*;
import static com.topjava.restaurantvoiting.model.AbstractBaseEntity.START_SEQ;

public class FoodDataTest {
    public static final MatchFactory.Matcher<Food> FOOD_MATCHER = MatchFactory.usingIgnoringFieldsComparator(
            Food.class, "restaurant");

    public static final MatchFactory.Matcher<FoodTo> FOOD_TO_MATCHER = MatchFactory.usingEqualsComparator(
            FoodTo.class);
    public static final int NOT_FOUND = 11;
    public static final int FOOD1_ID = START_SEQ + 8;

    public static final Food food1 = new Food(FOOD1_ID, LocalDate.now(), "Tagliolini Thai", new BigDecimal("46.90"), restaurant1);
    public static final Food food2 = new Food(FOOD1_ID + 1, LocalDate.now(), "Pork chop in Old Polish", new BigDecimal("49.90"), restaurant1);
    public static final Food food3 = new Food(FOOD1_ID + 2, LocalDate.now(), "Grilled beef tenderloin steak", new BigDecimal("55.90"), restaurant1);
    public static final Food food4 = new Food(FOOD1_ID + 3, LocalDate.now(), "Tagliolini nero with shrimp and chorizo", new BigDecimal("45.90"), restaurant1);
    public static final Food food5 = new Food(FOOD1_ID + 4, LocalDate.now(), "Roasted ribs in spicy marinade", new BigDecimal("55.90"), restaurant1);

    public static final Food food6 = new Food(FOOD1_ID + 5, LocalDate.now(), "Sweet dumplings with raspberries and white chocolate", new BigDecimal("35.00"), restaurant2);
    public static final Food food7 = new Food(FOOD1_ID + 6, LocalDate.now(), "Traditional dumplings with chopped spinach", new BigDecimal("35.50"), restaurant2);
    public static final Food food8 = new Food(FOOD1_ID + 7, LocalDate.now(), "Dumplings Mandu", new BigDecimal("37.00"), restaurant2);
    public static final Food food9 = new Food(FOOD1_ID + 8, LocalDate.now(), "Tomato soup with handmade egg noodles", new BigDecimal("11.50"), restaurant2);
    public static final Food food10 = new Food(FOOD1_ID + 9, LocalDate.now(), "Dumplings from the stove with spicy chicken", new BigDecimal("46.90"), restaurant2);

    public static final Food food11 = new Food(FOOD1_ID + 10, LocalDate.now(), "Forfiter", new BigDecimal("38.90"), restaurant3);
    public static final Food food12 = new Food(FOOD1_ID + 11, LocalDate.now(), "Caramel with chips and lemonade", new BigDecimal("52.90"), restaurant3);
    public static final Food food13 = new Food(FOOD1_ID + 12, LocalDate.now(), "Boss", new BigDecimal("42.90"), restaurant3);
    public static final Food food14 = new Food(FOOD1_ID + 13, LocalDate.now(), "American with chips and lemonade", new BigDecimal("54.90"), restaurant3);
    public static final Food food15 = new Food(FOOD1_ID + 14, LocalDate.now(), "Double Trouble with chips and lemonade", new BigDecimal("52.90"), restaurant3);

    public static final Food food16 = new Food(FOOD1_ID + 15, LocalDate.of(20023, 7, 7), "Leek risotto with crayfish meat", new BigDecimal("48.00"), restaurant4);
    public static final Food food17 = new Food(FOOD1_ID + 16, LocalDate.of(20023, 7, 7), "Grilled dry watermelon", new BigDecimal("78.00"), restaurant4);
    public static final Food food18 = new Food(FOOD1_ID + 17, LocalDate.of(20023, 7, 7), "Beef tenderloin", new BigDecimal("169.00"), restaurant4);
    public static final Food food19 = new Food(FOOD1_ID + 18, LocalDate.of(20023, 7, 7), "Sturgeon fillet", new BigDecimal("115.00"), restaurant4);
    public static final Food food20 = new Food(FOOD1_ID + 19, LocalDate.of(20023, 7, 7), "Masurian Zander", new BigDecimal("86.00"), restaurant4);

    public static final List<Food> foodsByRestaurant = List.of(food1, food2, food3, food4, food5);
    public static final List<Food> foodsByDate = List.of(food16, food17, food18, food19, food20);
    public static final List<Food> foods = List.of(food1, food2, food3, food4, food5, food6, food7, food8, food9, food10,
            food11, food12, food13, food14, food15, food16, food17, food18, food19, food20);
    public static final List<Food> menuOfDay = List.of(food11, food12, food13, food14, food15);
    public static Food getNew(){
        return new Food(null,LocalDate.now().plusDays(7),"NewFood",new BigDecimal("1.23"),restaurant1);
    }
    public static Food getUpdated(){
        return new Food(FOOD1_ID,food1.getPrepDate().plusDays(1),"UpdatedFood",new BigDecimal("49.56"),food1.getRestaurant());
    }
}

