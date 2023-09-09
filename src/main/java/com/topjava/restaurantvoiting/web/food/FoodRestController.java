package com.topjava.restaurantvoiting.web.food;

import com.topjava.restaurantvoiting.model.Food;
import com.topjava.restaurantvoiting.service.FoodService;
import com.topjava.restaurantvoiting.to.FoodTo;
import com.topjava.restaurantvoiting.util.FoodUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static com.topjava.restaurantvoiting.util.ValidationUtil.assureIdConsistent;
import static com.topjava.restaurantvoiting.util.ValidationUtil.checkNew;


@RestController
@RequestMapping(value = FoodRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class FoodRestController {
    static final String REST_URL = "/api/admin/foods";
    private final FoodService foodService;

    public FoodRestController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping(value = "/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FoodTo> createWithLocation(@Valid @RequestBody Food food, @RequestParam int restaurantId) {
        log.info("create food {} for restaurant {}", food, restaurantId);
        checkNew(food);
        FoodTo created = FoodUtil.createTo(foodService.create(food, restaurantId));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/{id}")
    public FoodTo get(@PathVariable int id) {
        log.info("get food {}", id);
        return FoodUtil.createTo(foodService.get(id));
    }

    @GetMapping("/by-date")
    public List<FoodTo> getAllByDate(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                     @RequestParam LocalDate prepDate) {
        log.info("getAllByDate {}", prepDate);
        return FoodUtil.getTos(foodService.getAllByDate(prepDate));
    }

    @GetMapping("/by-restaurant")
    public List<FoodTo> getAllByRestaurant(@RequestParam int restaurantId) {
        log.info("getAllByRestaurant {}", restaurantId);
        return FoodUtil.getTos(foodService.getAllByRestaurant(restaurantId));
    }

    @GetMapping("/by-restaurant-date")
    public List<FoodTo> getAllByRestaurantAndDate(@RequestParam int restaurantId,
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                  @RequestParam LocalDate prepDate) {
        log.info("getAllByRestaurant {} by date {}", restaurantId, prepDate);
        return FoodUtil.getTos(foodService.getAllByRestaurantAndDate(restaurantId, prepDate));
    }

    @GetMapping()
    public List<FoodTo> getAll() {
        log.info("getAll");
        return FoodUtil.getTos(foodService.getAll());
    }

    @PutMapping(value = "/restaurant", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Food food,
                       @RequestParam int restaurantId) {
        log.info("update food {} for restaurant {}", food, restaurantId);
        assureIdConsistent(food, food.id());
        foodService.update(food, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete food {}", id);
        foodService.delete(id);
    }
}
