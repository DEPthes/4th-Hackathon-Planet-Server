package com.depth.planet.domain.quest.entity;

import com.depth.planet.common.auditor.TimeStampedEntity;
import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
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

    public static Quest of(QuestDto.QuestSuggestionResponse suggestion, User user) {
        return Quest.builder()
                .title(suggestion.getTitle())
                .encouragement(suggestion.getEncouragement())
                .user(user)
                .build();
    }

    public void complete() {
        this.completedAt = LocalDateTime.now();
        this.isCompleted = true;
    }

    @OneToOne(fetch = FetchType.LAZY)
    private EvidenceImage evidenceImage;
}
