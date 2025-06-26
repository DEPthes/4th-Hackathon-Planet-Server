package com.depth.planet.domain.user.repository;

import com.depth.planet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
