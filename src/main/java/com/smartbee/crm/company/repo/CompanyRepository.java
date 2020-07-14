package com.smartbee.crm.company.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
    public interface CompanyRepository extends PagingAndSortingRepository<CrmCompany, UUID> {

    Optional<List<CrmCompany>> findByName(String name);

    Page<CrmCompany> findByName(String name, Pageable pageable);
}
