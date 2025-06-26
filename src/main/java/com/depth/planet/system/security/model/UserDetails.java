package com.depth.planet.system.security.model;

import com.depth.planet.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;

@RequiredArgsConstructor
@Getter
@Builder
public class UserDetails extends AuthDetails{
    private final User user;

    @Override
    public String getKey() {
        return user.getEmail();
    }

    public static UserDetails from(User user) {
        User unproxied = Hibernate.unproxy(user, User.class);

        return UserDetails.builder()
                .user(unproxied)
                .build();
    }
}
