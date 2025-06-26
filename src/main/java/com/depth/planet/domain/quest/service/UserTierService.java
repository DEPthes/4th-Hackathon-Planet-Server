package com.depth.planet.domain.quest.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.depth.planet.domain.quest.dto.TierDto;
import com.depth.planet.domain.quest.entity.UserTier;
import com.depth.planet.domain.quest.entity.embeddable.UserTierId;
import com.depth.planet.domain.quest.entity.enums.TierType;
import com.depth.planet.domain.quest.repository.UserTierRepository;
import com.depth.planet.domain.user.entity.User;
import com.depth.planet.domain.user.repository.UserRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserTierService {
    private final UserTierRepository userTierRepository;
    private final UserRepository userRepository;

    public TierDto.TierResponse addExpToUser(String email, int exp) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        UserTierId userTierId = UserTierId.builder()
                .user(user)
                .year(LocalDate.now().getYear())
                .month(LocalDate.now().getMonthValue())
                .build();

        // 기존 UserTier 조회 또는 새로 생성
        UserTier userTier = userTierRepository.findById(userTierId)
                .orElse(UserTier.builder()
                        .id(userTierId)
                        .tier(TierType.TinyStar)
                        .experiencePoint(0L)
                        .build());

        // 경험치 추가
        long newExp = userTier.getExperiencePoint() + exp;
        userTier.setExperiencePoint(newExp);

        // 새로운 티어 계산
        TierType newTier = calculateTierFromExp(newExp);
        userTier.setTier(newTier);

        // 저장
        userTier = userTierRepository.save(userTier);

        return TierDto.TierResponse.from(userTier);
    }

    private TierType calculateTierFromExp(long exp) {
        TierType[] tiers = TierType.values();
        TierType currentTier = TierType.TinyStar;

        for (TierType tier : tiers) {
            if (exp >= tier.getExp()) {
                currentTier = tier;
            } else {
                break;
            }
        }

        return currentTier;
    }

    public TierDto.TierResponse findThisMonthTier(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        UserTierId userTierId = UserTierId.builder()
                .user(user)
                .year(LocalDate.now().getYear())
                .month(LocalDate.now().getMonthValue())
                .build();

        UserTier userTier = userTierRepository.findById(userTierId)
                .orElse(UserTier.builder()
                        .id(userTierId)
                        .tier(TierType.TinyStar)
                        .experiencePoint(0L)
                        .build());
        return TierDto.TierResponse.from(userTier);
    }

    public TierDto.TierResponse findSpecificMonthTier(String email,Integer year,Integer month) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        UserTierId userTierId = UserTierId.builder()
                .user(user)
                .year(year)
                .month(month)
                .build();

        UserTier userTier = userTierRepository.findById(userTierId)
                .orElse(UserTier.builder()
                        .id(userTierId)
                        .tier(TierType.TinyStar)
                        .experiencePoint(0L)
                        .build());

        return TierDto.TierResponse.from(userTier);
    }
}
