package com.depth.planet.domain.quest.dto;


import com.depth.planet.domain.quest.entity.UserTier;
import com.depth.planet.domain.quest.entity.enums.TierType;
import com.depth.planet.domain.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TierDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class TierResponse {
        private TierType tier;
        private UserDto.UserResponse user;
        private Long experiencePoint;
        private Integer year;
        private Integer month;

        public static TierResponse from(UserTier tier) {
            return TierResponse.builder()
                    .tier(tier.getTier())
                    .user(UserDto.UserResponse.from(tier.getId().getUser()))
                    .experiencePoint(tier.getExperiencePoint())
                    .year(tier.getId().getYear())
                    .month(tier.getId().getMonth())
                    .build();
        }
    }
}
