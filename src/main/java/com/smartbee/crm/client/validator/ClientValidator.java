package com.smartbee.crm.client.validator;

import com.smartbee.crm.company.repo.CrmCompany;
import com.smartbee.crm.company.CompanyService;
import com.smartbee.crm.exception.DataNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Component
public class ClientValidator {

    private final CompanyService companyService;

    public ClientValidator(final CompanyService companyService) {
        this.companyService = companyService;
    }

    public void validateCompanyId(final UUID companyId) {
        companyService.findCompanyById(companyId);
    }
}
