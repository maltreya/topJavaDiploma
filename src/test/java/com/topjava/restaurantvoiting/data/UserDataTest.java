package com.topjava.restaurantvoiting.data;

import com.topjava.restaurantvoiting.MatchFactory;
import com.topjava.restaurantvoiting.model.Role;
import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.to.UserTo;
import com.topjava.restaurantvoiting.util.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static com.topjava.restaurantvoiting.model.AbstractBaseEntity.START_SEQ;

public class UserDataTest {
    public static final MatchFactory.Matcher<User> USER_MATCHER = MatchFactory.usingIgnoringFieldsComparator(
            User.class, "registered", "roles", "votes", "password");

    public static final MatchFactory.Matcher<UserTo> USER_TO_MATCHER = MatchFactory.usingEqualsComparator(
            UserTo.class);

    public static final int USER1_ID = START_SEQ;
    public static final int USER2_ID = START_SEQ + 1;
    public static final int USER3_ID = START_SEQ + 2;
    public static final int ADMIN_ID = START_SEQ + 3;

    public static final int NOT_FOUND = 11;

    public static final User user1 = new User(USER1_ID,"Brad Pitt", "bradpitt@gmail.com", "password", Role.USER);
    public static final User user2 = new User(USER2_ID,"Christoph Waltz", "christophwalttz@gmail.com", "password", Role.USER);
    public static final User user3 = new User(USER3_ID,"Michael Fassbender", "michaelfassbender@gmail.com", "password", Role.USER);
    public static final User admin = new User(ADMIN_ID,"Eli Roth", "eliroth@gmail.com", "password", Role.ADMIN);

    public static User getNew(){
        return new User(null, "NewName","newMail@gmail.com","newPassword",
                false,new Date(), Collections.singleton(Role.USER));
    }
    public static User getUpdated(){
        User updated = new User(user1);
        updated.setEmail("updatedMail@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("updatedPassword");
        updated.setEnabled(false);
        updated.setRoles(Collections.singleton(Role.ADMIN));

        return updated;
    }
    public static String jsonWithPassword(User user, String password){
        return JsonUtil.writeAdditionProps(user,"password",password);
    }
}

