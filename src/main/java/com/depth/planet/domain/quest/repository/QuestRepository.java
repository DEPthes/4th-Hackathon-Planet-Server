package com.depth.planet.domain.quest.repository;

import com.depth.planet.domain.quest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
}
