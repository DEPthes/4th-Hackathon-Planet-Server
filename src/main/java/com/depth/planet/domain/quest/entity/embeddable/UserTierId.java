package com.depth.planet.domain.quest.entity.embeddable;

import java.io.Serializable;
import java.util.Objects;

import com.depth.planet.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Embeddable
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTierId {
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "YEAR_NUMBER")
    private int year;

    @Column(name = "MONTH_NUMBER")
    private int month;
}
