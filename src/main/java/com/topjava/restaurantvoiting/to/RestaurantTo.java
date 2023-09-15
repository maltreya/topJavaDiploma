package com.topjava.restaurantvoiting.to;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.topjava.restaurantvoiting.model.Food;

import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RestaurantTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1l;
    private String name;
    private List<Food> foods;

    @ConstructorProperties({"id", "name"})
    public RestaurantTo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @ConstructorProperties({"id", "name", "menu"})
    public RestaurantTo(Integer id, String name, List<Food> foods) {
        this.id = id;
        this.name = name;
        this.foods = foods;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, foods);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RestaurantTo that = (RestaurantTo) obj;
        return name.equals(that.name) && Objects.equals(foods, that.foods);
    }
}
