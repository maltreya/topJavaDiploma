package com.topjava.restaurantvoiting.service;

import com.topjava.restaurantvoiting.model.Role;
import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Set;

import static com.topjava.restaurantvoiting.data.UserDataTest.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest extends AbstractServiceTest {
    @Autowired
    protected UserService userService;

    @Test
    void create() {
        User created = userService.create(getNew());
        int newId = created.id();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class,
                () -> userService.create(new User(
                        null, "Duplicate", user1.getEmail(), "newPassword", Role.USER)));
    }

    @Test
    void delete() {
        userService.delete(USER1_ID);
        assertThrows(NotFoundException.class,
                () -> userService.get(USER1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class,
                () -> userService.delete(NOT_FOUND));
    }

    @Test
    void get() {
        User user = userService.get(USER1_ID);
        USER_MATCHER.assertMatch(user, user1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class,
                () -> userService.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = userService.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void update() {
        User updated = getUpdated();
        userService.update(updated);
        USER_MATCHER.assertMatch(userService.get(USER1_ID), getUpdated());
    }

    @Test
    void getAll() {
        List<User> allUsers = userService.getAll();
        USER_MATCHER.assertMatch(allUsers, admin, user3, user2, user1);
    }

    @Test
    void createWithException() throws Exception {
        validateRootCause(ConstraintViolationException.class,
                () -> userService.create(
                        new User(null, " ", "newUser@gmail.com", "password", Role.USER)
                ));

        validateRootCause(ConstraintViolationException.class,
                () -> userService.create(
                        new User(null, "NewUser", " ", "password", Role.USER)
                ));

        validateRootCause(ConstraintViolationException.class,
                () -> userService.create(new User(
                        null, "NewUser", "bradpitt@gmail.com", "password",
                        true, null, Set.of()
                )));
    }
}
