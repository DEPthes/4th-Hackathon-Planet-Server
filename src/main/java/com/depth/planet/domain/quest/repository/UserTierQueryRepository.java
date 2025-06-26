package com.depth.planet.domain.quest.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserTierQueryRepository {
    private final JPAQueryFactory queryFactory;


}
