package com.topjava.restaurantvoiting.web.vote;

import com.topjava.restaurantvoiting.AuthUser;
import com.topjava.restaurantvoiting.View;
import com.topjava.restaurantvoiting.model.Restaurant;
import com.topjava.restaurantvoiting.service.VoteService;
import com.topjava.restaurantvoiting.to.VoteTo;
import com.topjava.restaurantvoiting.util.VoteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping(value = VoteProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class VoteProfileRestController {
    static final String REST_URL = "/api/profile/votes";
    private final VoteService voteService;

    public VoteProfileRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VoteTo> createWithLocation(@Validated(View.Validated.class)
                                                     @RequestBody Restaurant restaurant,
                                                     @AuthenticationPrincipal AuthUser authUser) {
        log.info("create vote from user {} for restaurant {}", authUser.id(), restaurant.id());
        VoteTo created = VoteUtil.createTo(voteService.create(authUser.id(), restaurant.id()));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
    @GetMapping()
    public VoteTo get(@AuthenticationPrincipal AuthUser authUser){
        log.info("get {}",authUser.id());
        return VoteUtil.createTo(voteService.getByUserAndDate(authUser.id(), LocalDate.now()));
    }
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id,
                       @Validated(View.Validated.class) @RequestBody Restaurant restaurant){
        log.info("update vote {} for restaurant {}", id, restaurant.id());
        voteService.update(id, restaurant.id());
    }
}
