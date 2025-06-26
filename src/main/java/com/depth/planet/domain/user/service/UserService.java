package com.depth.planet.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.depth.planet.domain.user.dto.UserDto;
import com.depth.planet.domain.user.dto.UserDto.UserResponse;
import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.entity.enums.Role;
import com.depth.planet.domain.user.repository.UserRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto.UserResponse updateUser(String email, UserDto.UserUpdateRequest request, UserDetails userDetails) {
        cannotUpdateIfNotMyself(email, userDetails);

        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (StringUtils.hasText(request.getName())) {
            user.setName(request.getName());
        }
        if (request.getMbti() != null) {
            user.setMbti(request.getMbti());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getHobbies() != null) {
            user.setHobbies(request.getHobbies());
        }

        return UserDto.UserResponse.from(user);
    }

    private static void cannotUpdateIfNotMyself(String email, UserDetails userDetails) {
        if (userDetails.getUser().getRole().equals(Role.Admin)) {
            return;
        }

        if (userDetails.getUser().getEmail().equals(email)) {
            return;
        }

        throw new RestException(ErrorCode.AUTH_FORBIDDEN);
    }

    @Transactional(readOnly = true)
    public UserDto.UserResponse findUserByEmail(String email, UserDetails userDetails) {
        if (!userDetails.getUser().getRole().equals(Role.Admin)) {
            throw new RestException(ErrorCode.AUTH_FORBIDDEN);
        }
        return userRepository.findById(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));
    }
}
