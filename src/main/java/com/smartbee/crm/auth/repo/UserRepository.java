package com.smartbee.crm.auth.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<CrmUser, UUID> {

    Optional<CrmUser> findByName(String name);
}
