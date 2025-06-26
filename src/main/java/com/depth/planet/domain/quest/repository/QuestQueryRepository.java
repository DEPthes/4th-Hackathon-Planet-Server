package com.depth.planet.domain.quest.repository;

import static com.depth.planet.domain.quest.entity.QQuest.quest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.depth.planet.domain.quest.entity.Quest;
import com.depth.planet.system.security.model.UserDetails;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestQueryRepository {
        private final JPAQueryFactory queryFactory;

        public List<Quest> findBetween(LocalDate startDate, LocalDate endDate, UserDetails user) {
                return queryFactory.selectFrom(quest)
                                .where(
                                                quest.isCompleted.eq(true),
                                                quest.completedAt.between(startDate.atStartOfDay(),
                                                                endDate.plusDays(1).atStartOfDay()),
                                                quest.user.email.eq(user.getKey()))
                                .fetch();
        }

        public Optional<Quest> findToday(UserDetails user) {

                return Optional.ofNullable(
                                queryFactory.selectFrom(quest)
                                                .where(
                                                                quest.user.email.eq(user.getKey()),
                                                                quest.createdAt.between(LocalDate.now().atStartOfDay(),
                                                                                LocalDateTime.now()))
                                                .fetchFirst());
        }

        public List<Quest> findAllByUser(UserDetails user) {
                return queryFactory.selectFrom(quest)
                                .leftJoin(quest.evidenceImage).fetchJoin()
                                .where(quest.user.email.eq(user.getKey()))
                                .fetch();
        }
}
