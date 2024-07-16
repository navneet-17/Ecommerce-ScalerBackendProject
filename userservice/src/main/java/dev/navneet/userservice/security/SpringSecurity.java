package dev.navneet.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurity {
    // TODO: implement Spring Security Filter Chain here
//    @Bean
//    @Order(1)
//    public SecurityFilterChain filteringCriteria(HttpSecurity http) throws Exception {
//        http.cors().disable();
//        http.csrf().disable();
//        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
////        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
////        http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/auth/*").authenticated());
//        return http.build();
//    }
    // Object that handles what all api endpoints should be authenticated
    // v/s what all shouldn't be authenticated

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
