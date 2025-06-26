package com.depth.planet.domain.file.repository;

import com.depth.planet.domain.file.entity.AttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachedFileRepository extends JpaRepository<AttachedFile, String> {
}
