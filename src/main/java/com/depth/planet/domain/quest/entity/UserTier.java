package com.depth.planet.domain.quest.entity;

import com.depth.planet.domain.quest.entity.embeddable.UserTierId;
import com.depth.planet.domain.quest.entity.enums.TierType;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTier {
    @EmbeddedId
    @Setter(AccessLevel.NONE)
    private UserTierId id;

    @Enumerated(EnumType.STRING)
    private TierType tier;

    @Builder.Default
    private long experiencePoint = 0;
}
