package com.smartbee.crm.client.validator;

import com.smartbee.crm.company.CompanyService;
import org.springframework.stereotype.Component;

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
