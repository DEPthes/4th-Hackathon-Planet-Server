package com.depth.planet.domain.quest.repository;

import com.depth.planet.domain.quest.entity.UserTier;
import com.depth.planet.domain.quest.entity.embeddable.UserTierId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTierRepository extends JpaRepository<UserTier, UserTierId> {
}
