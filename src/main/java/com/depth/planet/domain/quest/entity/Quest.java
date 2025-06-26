package com.depth.planet.domain.quest.entity;

import java.time.LocalDateTime;

import com.depth.planet.common.auditor.TimeStampedEntity;
import com.depth.planet.domain.file.entity.EvidenceImage;
import com.depth.planet.domain.quest.dto.QuestDto;
import com.depth.planet.domain.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Quest extends TimeStampedEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String encouragement;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder.Default
    private Boolean isCompleted = false;

    private LocalDateTime completedAt;

    private String feedback;

    public static Quest of(QuestDto.QuestSuggestionResponse suggestion, User user, String encouragement) {
        return Quest.builder()
                .title(suggestion.getTitle())
                .encouragement(encouragement)
                .user(user)
                .build();
    }

    public void complete() {
        this.completedAt = LocalDateTime.now();
        this.isCompleted = true;
    }

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private EvidenceImage evidenceImage;
}
