package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.repository.RestaurantRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.topjava.restaurantvoiting.util.ValidationUtil.checkExisted;
import static com.topjava.restaurantvoiting.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RestaurantService {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC,"name");
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public Restaurant create(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant should not be null");
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public List<Restaurant> getAllWithMenu() {
        return repository.getAllWithMenu();
    }

    @Transactional
    public Restaurant getMenuOfDay(int id) {
        Restaurant restaurant = checkNotFoundWithId(repository.getMenuOfDay(id), id);
        Assert.notNull(restaurant, "restaurant should not be null");
        return restaurant;
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant should not be null");
        checkExisted(repository, restaurant.id());
        checkNotFoundWithId(repository.save(restaurant), restaurant.id());
    }

    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }
}
