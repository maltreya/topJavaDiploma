package com.topjava.restaurantvoiting.web.user;

import com.topjava.restaurantvoiting.data.UserDataTest;
import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.service.UserService;
import com.topjava.restaurantvoiting.util.exception.NotFoundException;
import com.topjava.restaurantvoiting.web.vote.AbstractControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.topjava.restaurantvoiting.UtilTest.userHttpBasic;
import static com.topjava.restaurantvoiting.data.UserDataTest.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AdminRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/admin/users";
    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL + '/' + ADMIN_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(admin));
    }
    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/by-email?email="+user1.getEmail())
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+'/'+USER1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class,
                () -> userService.get(USER1_ID));
    }
    @Test
    void update() throws Exception {
        User updated = UserDataTest.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + '/' + USER1_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(updated,updated.getPassword())))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER1_ID),updated);
    }
    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(List.of(admin,user1,user2,user3)));
    }
    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isForbidden());
    }
    @Test
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL+'/'+NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
    @Test
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL+'/'+NOT_FOUND)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        User updated = new User(user1);
        updated.setEmail("admin@gmail.com");
        perform(MockMvcRequestBuilders.put(REST_URL+'/'+USER1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(updated,"password")))
                .andDo(print())
                .andExpect(status().isConflict());
    }
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void createDuplicate() throws Exception{
        User expected= new User(null,"NewName","bradpitt@gmail.com","newPassword");
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(expected,"newPassword")))
                .andDo(print())
                .andExpect(status().isConflict());
    }
    @Test
    void createLocation() throws Exception {
        User newUser = UserDataTest.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(admin))
                .content(jsonWithPassword(newUser, newUser.getPassword())))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created,newUser);
        USER_MATCHER.assertMatch(userService.get(newId),newUser);
    }
}
