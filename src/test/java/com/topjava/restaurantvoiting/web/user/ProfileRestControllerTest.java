package com.topjava.restaurantvoiting.web.user;

import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.service.UserService;
import com.topjava.restaurantvoiting.to.UserTo;
import com.topjava.restaurantvoiting.util.JsonUtil;
import com.topjava.restaurantvoiting.util.UserUtil;
import com.topjava.restaurantvoiting.web.vote.AbstractControllerTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.topjava.restaurantvoiting.UtilTest.userHttpBasic;
import static com.topjava.restaurantvoiting.data.UserDataTest.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class ProfileRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = "/api/profile";

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user1));
    }
    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user1)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userService.getAll(),admin,user2,user3);
    }
    @Test
    void update() throws Exception {
        UserTo updatedTo = new UserTo(null,"NewName","newUser@gmail.com","newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER1_ID), UserUtil.updateFromTo(new User(user1),updatedTo));
    }
    @Test
    void getUnauthorised() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void register() throws Exception {
        UserTo newTo = new UserTo(null,"NewName","newEmail@gmail.com","newPassword");
        User newUser = UserUtil.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created,newUser);
        USER_MATCHER.assertMatch(userService.get(newId),newUser);
    }
    @Test
    @Transactional(propagation = Propagation.NEVER)
    void updateDuplicate() throws Exception {
        UserTo updatedTo = new UserTo(null,"NewName","newuserdupl@gmail.com","newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user1))
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}
