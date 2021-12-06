package com.carzio.app.repository;

import com.carzio.app.models.ERole;
import com.carzio.app.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
