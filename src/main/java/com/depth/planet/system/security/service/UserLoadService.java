package com.depth.planet.system.security.service;


import com.depth.planet.system.security.model.AuthDetails;

import java.util.Optional;

public interface UserLoadService {
    Optional<? extends AuthDetails> loadUserByKey(String key);
}
