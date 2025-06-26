package com.depth.planet.domain.quest.entity;

import com.depth.planet.domain.quest.entity.embeddable.UserTierId;
import com.depth.planet.domain.quest.entity.enums.TierType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserTier {
    @Id @Setter(AccessLevel.NONE)
    private UserTierId id;

    @Enumerated(EnumType.STRING)
    private TierType tier;

    @Builder.Default
    private long experiencePoint = 0;
}
