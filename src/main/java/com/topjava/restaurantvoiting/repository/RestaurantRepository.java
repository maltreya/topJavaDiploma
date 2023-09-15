package com.topjava.restaurantvoiting.repository;

import com.topjava.restaurantvoiting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.foods")
    List<Restaurant> getAllWithMenu();

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.foods m WHERE r.id=:id AND m.prepDate=current_date")
    Restaurant getMenuOfDay(int id);
}
