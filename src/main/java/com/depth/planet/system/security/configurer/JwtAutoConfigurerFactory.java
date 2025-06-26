package com.depth.planet.system.security.configurer;

import com.depth.planet.system.security.service.UserLoadService;
import com.depth.planet.system.security.utility.JwtTokenResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerExceptionResolver;

@RequiredArgsConstructor
public class JwtAutoConfigurerFactory {
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtTokenResolver jwtTokenResolver;

    public JwtAutoConfigurer create(UserLoadService userLoadService) {
        return new JwtAutoConfigurer(jwtTokenResolver, userLoadService, handlerExceptionResolver);
    }
}
