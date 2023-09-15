package com.topjava.restaurantvoiting.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.topjava.restaurantvoiting.model.Role;
import com.topjava.restaurantvoiting.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.topjava.restaurantvoiting.repository.UserRepository;
import com.topjava.restaurantvoiting.AuthUser;

import java.util.Optional;

@Configuration
@EnableWebSecurity
@Slf4j
@AllArgsConstructor
public class SecurityConfiguration {
    public static final PasswordEncoder PASSWORD_ENCODER = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PASSWORD_ENCODER;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(
                        email -> {
                            log.debug("Authenticating '{}'", email);
                            Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
                            return new AuthUser(optionalUser.orElseThrow(
                                    () -> new UsernameNotFoundException("User '" + email + "' was not found")
                            ));
                        })
                .passwordEncoder(PASSWORD_ENCODER);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, "api/profile").anonymous()
                .requestMatchers("/api/**").authenticated()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return http.build();
    }
}
