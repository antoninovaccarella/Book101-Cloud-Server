package com.antonino.book101server.repositories;

import com.antonino.book101server.models.Role;
import com.antonino.book101server.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRole name);
    Boolean existsByName(UserRole name);
}
