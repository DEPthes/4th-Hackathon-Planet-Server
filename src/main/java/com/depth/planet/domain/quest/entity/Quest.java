package com.depth.planet.domain.quest.entity;

import com.depth.planet.common.auditor.TimeStampedEntity;
import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    public void complete() {
        this.completedAt = LocalDateTime.now();
        this.isCompleted = true;
    }

    @OneToOne(fetch = FetchType.LAZY)
    private EvidenceImage evidenceImage;
}
