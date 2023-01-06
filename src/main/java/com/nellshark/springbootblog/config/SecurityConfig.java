package com.nellshark.springbootblog.config;

import com.nellshark.springbootblog.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //    @formatter:off
        http
                .csrf().disable().httpBasic().and() // DELETE
                .authorizeRequests()
                .and()
                .formLogin()
                    .loginPage("/users/sign-in")
                    .permitAll()
                    .usernameParameter("email")
                    .passwordParameter("password")
                .and()
                .rememberMe()
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/users/sign-out")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID");
        return http.build();
        //    @formatter:on
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userService);
        return daoAuthenticationProvider;
    }
}
