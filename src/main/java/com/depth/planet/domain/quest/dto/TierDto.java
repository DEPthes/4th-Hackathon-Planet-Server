package com.depth.planet.domain.quest.dto;

import com.depth.planet.domain.quest.entity.UserTier;
import com.depth.planet.domain.quest.entity.enums.TierType;
import com.depth.planet.domain.user.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class TierDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "경험치 추가 요청 DTO")
    public static class AddExpRequest {
        @Schema(description = "추가할 경험치", example = "100")
        private Integer exp;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "티어 정보 응답 DTO")
    public static class TierResponse {
        @Schema(description = "현재 티어", example = "SmallStar")
        private TierType tier;

        @Schema(description = "사용자 정보")
        private UserDto.UserResponse user;

        @Schema(description = "현재 경험치", example = "1500")
        private Long experiencePoint;

        @Schema(description = "년도", example = "2024")
        private Integer year;

        @Schema(description = "월", example = "12")
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
