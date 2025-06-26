package com.depth.planet.domain.quest.service;

import com.depth.planet.domain.quest.repository.UserTierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserTierService {
    private final UserTierRepository userTierRepository;

    //TODO: 유저에게 exp 추가하는 메서드(exp가 갱신됐을때 현재 exp양에 따라 티어도 갱신될 수 있음)

    //TODO: 유저의 이번달 티어를 가져오는 메서드

    //TODO: 유저의 특정 달 티어를 가져오는 메서드
}
