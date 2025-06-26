package com.depth.planet.domain.quest.service;

import com.depth.planet.domain.quest.dto.TierDto;
import com.depth.planet.domain.quest.repository.UserTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTierService {
    private final UserTierRepository userTierRepository;

    public TierDto.TierResponse addExpToUser(String email, int exp) {
        return null;
    }

    //TODO: 유저의 이번달 티어를 가져오는 메서드

    //TODO: 유저의 특정 달 티어를 가져오는 메서드
}
