package com.topjava.restaurantvoiting.web.restaurant;

import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.repository.RestaurantRepository;
import com.topjava.restaurantvoiting.service.RestaurantService;
import com.topjava.restaurantvoiting.to.RestaurantTo;
import com.topjava.restaurantvoiting.util.RestaurantUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.topjava.restaurantvoiting.util.ValidationUtil.assureIdConsistent;
import static com.topjava.restaurantvoiting.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantAdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantAdminRestController {
    static final String REST_URL = "/api/admin/restaurants";
    private final RestaurantService restaurantService;

    public RestaurantAdminRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantTo> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        RestaurantTo created = RestaurantUtil.createTo(restaurantService.create(restaurant));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable int id){
        log.info("get {}", id);
        return RestaurantUtil.createTo(restaurantService.get(id));
    }
    @GetMapping()
    public List<RestaurantTo> getAll(){
        log.info("getAll");
        return RestaurantUtil.getTos(restaurantService.getAll());
    }
    @GetMapping("/with-menu")
    public List<RestaurantTo> getAllWithMenu(){
        log.info("getAllWithMenu");
        return RestaurantUtil.getTosWithMenu(restaurantService.getAllWithMenu());
    }
    @GetMapping("/{id}/menu")
    public RestaurantTo getMenuOfDay(@PathVariable int id){
        log.info("getMenuOfDay for restaurant {}",id);
        return RestaurantUtil.createToWithMenu(restaurantService.getMenuOfDay(id));
    }
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant){
        log.info("update {}", restaurant);
        assureIdConsistent(restaurant, restaurant.id());
        restaurantService.update(restaurant);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id){
        log.info("delete {}", id);
        restaurantService.delete(id);
    }
}
