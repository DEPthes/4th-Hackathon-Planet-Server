package com.depth.planet.domain.auth.service;

import com.depth.planet.domain.auth.dto.AuthDto;
import com.depth.planet.domain.user.dto.UserDto;
import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.repository.UserRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import com.depth.planet.system.security.model.UserDetails;
import com.depth.planet.system.security.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public AuthDto.SignInResponse signIn(AuthDto.SignInRequest request) {
        var found = userRepository.findById(request.getEmail())
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        if(!passwordEncoder.matches(request.getPassword(), found.getPassword()))
            throw new RestException(ErrorCode.AUTH_PASSWORD_NOT_MATCH);


        var token = jwtTokenProvider.issueToken(
                UserDetails.from(found),
                24 * 7L // expires in 1 week
        );

        return AuthDto.SignInResponse.of(
                token.getTokenString(),
                UserDto.UserResponse.from(found),
                token.getExpireAt()
        );
    }

    @Transactional
    public UserDto.UserResponse signUp(AuthDto.SignUpRequest request) {
        boolean isExisting = userRepository.existsByEmail(request.getEmail());
        if(isExisting)
            throw new RestException(ErrorCode.GLOBAL_ALREADY_EXIST);

        User toSave = request.toEntity(passwordEncoder);
        User saved = userRepository.save(toSave);

        return UserDto.UserResponse.from(saved);
    }
}
