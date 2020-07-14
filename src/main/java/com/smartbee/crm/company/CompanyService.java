package com.smartbee.crm.company;

import com.smartbee.crm.auth.JwtUserDetailsService;
import com.smartbee.crm.company.repo.CompanyRepository;
import com.smartbee.crm.company.repo.CrmCompany;
import com.smartbee.crm.exception.DataNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final JwtUserDetailsService userService;
    private final CompanyRepository companyRepository;

    public CompanyService(final JwtUserDetailsService userService, final CompanyRepository companyRepository) {
        this.userService = userService;
        this.companyRepository = companyRepository;
    }

    public List<CrmCompany> findCompanyByName(String name, Integer page, Integer size) {
        final List<CrmCompany> companies = new ArrayList<>();

        if (page == null || size == null) {
            companyRepository.findByName(name).ifPresent(companies::addAll);
        } else {
            final Pageable pageable = PageRequest.of(page, size);
            final Page<CrmCompany> companyPage = companyRepository.findByName(name, pageable);
            companyPage.getContent()
                    .stream().collect(Collectors.toCollection(() -> companies));
        }

        return companies;
    }

    public CrmCompany findCompanyById(final UUID id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(CrmCompany.class, id.toString() + " does not found."));
    }

    public CrmCompany saveCompany(final CrmCompany company) {
        final UUID loginUser = UUID.fromString(userService.getLoginUser());
        final LocalDateTime now = LocalDateTime.now();
        company.setId(UUID.randomUUID());
        company.setCreatedBy(loginUser);
        company.setCreatedAt(now);
        company.setUpdatedBy(loginUser);
        company.setUpdatedAt(now);

        return companyRepository.save(company);
    }

    public CrmCompany updateCompany(final CrmCompany updatedCompany) {
        final UUID loginUser = UUID.fromString(userService.getLoginUser());
        final UUID id = updatedCompany.getId();
        final CrmCompany company  = findCompanyById(id);
        company.setName(updatedCompany.getName());
        company.setAddress(updatedCompany.getAddress());
        company.setUpdatedAt(LocalDateTime.now());
        company.setUpdatedBy(loginUser);

        return companyRepository.save(company);
    }

    public void deleteCompany(final UUID id) {
        try {
            companyRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new DataNotFoundException(CrmCompany.class, id.toString() + " does not found.");
        }
    }
}
