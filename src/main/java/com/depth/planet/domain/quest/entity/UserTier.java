package com.depth.planet.domain.quest.entity;

import com.depth.planet.domain.quest.entity.embeddable.UserTierId;
import com.depth.planet.domain.quest.entity.enums.TierType;
import com.depth.planet.domain.user.entity.User;
import jakarta.persistence.*;
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
