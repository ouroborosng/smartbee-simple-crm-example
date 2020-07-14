package com.smartbee.crm.client.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
    public interface ClientRepository extends PagingAndSortingRepository<CrmClient, UUID> {

    Optional<List<CrmClient>> findByName(String name);

    Page<CrmClient> findByName(String name, Pageable pageable);
}
