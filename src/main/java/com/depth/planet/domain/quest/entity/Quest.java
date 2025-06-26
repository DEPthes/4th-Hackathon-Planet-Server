package com.depth.planet.domain.quest.entity;

import com.depth.planet.common.auditor.TimeStampedEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Quest extends TimeStampedEntity {
    @Id @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String encouragement;

    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    public void complete() {
        this.completedAt = LocalDateTime.now();
        this.isCompleted = true;
    }

    //TODO: 인증샷 파일 저장
}
