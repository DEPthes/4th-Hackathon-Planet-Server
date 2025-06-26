package com.depth.planet.domain.user.service;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.entity.enums.GenderType;
import com.depth.planet.domain.user.entity.enums.MBTI;
import com.depth.planet.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DummyAccountLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.existsByEmail("admin@test.com")) {
            return;
        }

        User admin = User.builder()
                .email("admin@test.com")
                .password("admin")
                .name("Admin")
                .mbti(MBTI.ENFJ)
                .gender(GenderType.Male)
                .hobbies(List.of("독서", "코딩", "영화감상"))
                .build();

        userRepository.save(admin);
    }
}
