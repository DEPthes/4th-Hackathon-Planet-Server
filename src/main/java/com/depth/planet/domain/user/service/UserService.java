package com.depth.planet.domain.user.service;

import com.depth.planet.domain.user.dto.UserDto;
import com.depth.planet.domain.user.dto.UserDto.UserResponse;
import com.depth.planet.domain.user.repository.UserRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto.UserResponse updateUser(String email, UserDto.UserUpdateRequest request, UserDetails user) {
        //TODO: not implemented
        return null;
    }

    @Transactional(readOnly = true)
    public UserDto.UserResponse findUserByEmail(String email) {
        return userRepository.findById(email)
                .map(UserResponse::from)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));
    }
}
