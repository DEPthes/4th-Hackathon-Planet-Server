package com.depth.planet.system.configuration;

import com.depth.planet.domain.user.service.UserLoadServiceImpl;
import com.depth.planet.system.security.configurer.JwtAutoConfigurerFactory;
import com.depth.planet.system.security.service.UserLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAutoConfigurerFactory jwtAutoConfigurerFactory;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, UserLoadService userLoadService) throws Exception {
        jwtAutoConfigurerFactory.create(userLoadService)
                .pathConfigure((it) -> {
                    it.includePath("/api/**");
                    it.excludePath("/api/auth/register");
                    it.excludePath("/api/auth/login");
                })
                .configure(http);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
