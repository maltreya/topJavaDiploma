package com.topjava.restaurantvoiting.to;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.topjava.restaurantvoiting.model.Role;
import com.topjava.restaurantvoiting.View;

import java.beans.ConstructorProperties;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserTo extends BaseTo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1l;

    @NotBlank
    @Size(min = 2, max = 128)
    @JsonView(View.JsonREST.class)
    private String name;

    @Email
    @NotBlank
    @Size(max = 128)
    @JsonView(View.JsonREST.class)
    private String email;

    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    @JsonView(View.JsonREST.class)
    private boolean enabled = true;

    @JsonView(View.JsonREST.class)
    private LocalDate registered = LocalDate.now(ZoneId.systemDefault());

    @JsonView(View.JsonREST.class)
    private Set<Role> roles;

    @ConstructorProperties({"id", "name", "email", "password"})
    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password, enabled, registered, roles);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserTo userTo = (UserTo) obj;
        return enabled == userTo.enabled
                && name.equals(userTo.name)
                && email.equals(userTo.email)
                && password.equals(userTo.password)
                && Objects.equals(registered, userTo.registered)
                && Objects.equals(roles, userTo.roles);
    }
}
