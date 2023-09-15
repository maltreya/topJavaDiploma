package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.repository.FoodRepository;
import com.topjava.restaurantvoiting.repository.RestaurantRepository;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.util.ValidationUtil.checkExisted;
import static com.topjava.restaurantvoiting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class FoodService {
    private final FoodRepository repository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public FoodService(FoodRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @CacheEvict(value = "foods", allEntries = true)
    @Transactional
    public Food create(Food food, int restaurantId) {
        Assert.notNull(food, "food should not be null");
        food.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant " + restaurantId + "not found")
        ));
        return repository.save(food);
    }

    public Food get(int id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Food " + id + "not found")
        );
    }

    @Cacheable("foods")
    public List<Food> getAll() {
        return repository.findAll();
    }

    //by restaurant
    @CacheEvict(value = "foods", allEntries = true)
    public List<Food> getAllByRestaurant(int restaurantId) {
        checkExisted(restaurantRepository, restaurantId);
        return repository.getAll(restaurantId);
    }

    //by date
    public List<Food> getAllByDate(LocalDate prepDate) {
        Assert.notNull(prepDate, "prepDate should not be null");
        List<Food> foods = repository.getAllByDate(prepDate);
        return checkExisted(foods, prepDate);
    }

    //by restaurant & date
    public List<Food> getAllByRestaurantAndDate(int restaurantId, LocalDate prepDate) {
        Assert.notNull(prepDate, "prepDate should not be null");
        List<Food> foods = repository.getAllByRestaurantAndDate(restaurantId, prepDate);
        return checkExisted(foods, prepDate, restaurantId);
    }

    @CacheEvict(value = "foods", allEntries = true)
    public void update(Food food, int restaurantId) {
        Assert.notNull(food, "food should not be null");
        food.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("Restaurant " + restaurantId + "not found")
        ));
        checkNotFoundWithId(repository.save(food), food.id());
    }

    @CacheEvict(value = "foods", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

}
