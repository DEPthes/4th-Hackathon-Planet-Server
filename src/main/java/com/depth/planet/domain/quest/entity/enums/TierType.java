package com.depth.planet.domain.quest.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TierType {
    TinyStar("작은별", 0, 0),
    ShinyStar("반짝별", 1, 100),
    WaveStar("물결별", 2, 200),
    PlanetStar("행성별", 3, 300),
    HappyGalaxy("해피은하", 4, 400);

    private final String name;
    private final int level;
    private final int exp;

    public TierType nextTierType() {
        if (this == HappyGalaxy) {
            return null; // No next tier after HappyGalaxy
        }
        return values()[this.ordinal() + 1];
    }
}
