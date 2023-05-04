package mvcrest.spring6mvcrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic(Customizer.withDefaults())
                .csrf().ignoringRequestMatchers("/api/**");
        return httpSecurity.build();
    }
}
