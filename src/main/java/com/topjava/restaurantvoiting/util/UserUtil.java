package com.topjava.restaurantvoiting.util;

import com.topjava.restaurantvoiting.model.Role;
import com.topjava.restaurantvoiting.model.User;
import com.topjava.restaurantvoiting.to.UserTo;
import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@UtilityClass
public class UserUtil {
    public static List<UserTo> getTos(Collection<User> users){
        return users.stream().map(UserUtil::createTo).toList();
    }

    public static UserTo createTo(User user) {
        LocalDate dateConverted = user.getRegistered().toInstant().
                atZone(ZoneId.systemDefault()).toLocalDate();
        UserTo userTo=new UserTo(user.getId(),user.getName(),user.getEmail(),user.getPassword());
        userTo.setEnabled(user.isEnabled());
        userTo.setRegistered(dateConverted);
        userTo.setRoles(user.getRoles());

        return userTo;
    }

    public static User createNewFromTo(UserTo userTo){
        return new User(null,userTo.getName(),userTo.getEmail(),userTo.getPassword(), Role.USER);
    }
    public static User updateFromTo(User user, UserTo userTo){
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail());
        user.setPassword(userTo.getPassword());

        return user;
    }
    public static User prepareToSave(User user, PasswordEncoder passwordEncoder){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
