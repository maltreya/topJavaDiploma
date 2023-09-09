package com.topjava.restaurantvoiting.web.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.topjava.restaurantvoiting.AuthUser;
import com.topjava.restaurantvoiting.View;
import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.service.UserService;
import com.topjava.restaurantvoiting.to.UserTo;
import com.topjava.restaurantvoiting.util.UserUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.topjava.restaurantvoiting.util.ValidationUtil.assureIdConsistent;


@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProfileRestController {
    static final String REST_URL = "/api/profile";
    private final UserService service;

    public ProfileRestController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @JsonView(View.JsonREST.class)
    public UserTo get(@AuthenticationPrincipal AuthUser authUser) {
        log.info("get {}", authUser.id());

        return UserUtil.createTo(service.get(authUser.id()));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody UserTo userTo, @AuthenticationPrincipal AuthUser authUser) {
        log.info("update {} with id={}", userTo, authUser.id());
        assureIdConsistent(userTo, authUser.id());
        service.update(userTo);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody UserTo userTo) {
        User created = service.create(UserUtil.createNewFromTo(userTo));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path(REST_URL).build().toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("delete {}", authUser.id());
        service.delete(authUser.id());
    }
}
