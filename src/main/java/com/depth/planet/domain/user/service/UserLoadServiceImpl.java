package com.depth.planet.domain.user.service;

import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.repository.UserRepository;
import com.depth.planet.system.security.model.AuthDetails;
import com.depth.planet.system.security.model.UserDetails;
import com.depth.planet.system.security.service.UserLoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLoadServiceImpl implements UserLoadService {
    private final UserRepository userRepository;

    @Override
    public Optional<? extends AuthDetails> loadUserByKey(String key) {
        Optional<User> foundUser = userRepository.findById(key);

        return foundUser
                .map(UserDetails::from);
    }
}
