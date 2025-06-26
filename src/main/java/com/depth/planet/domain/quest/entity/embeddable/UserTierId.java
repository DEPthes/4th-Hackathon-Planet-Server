package com.depth.planet.domain.quest.entity.embeddable;

import com.depth.planet.domain.user.entity.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTierId {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private int year;
    private int month;
}
