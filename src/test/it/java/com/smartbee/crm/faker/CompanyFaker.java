package com.smartbee.crm.faker;

import com.github.javafaker.Faker;
import com.smartbee.crm.company.controller.CompanyVO;
import com.smartbee.crm.company.repo.CrmCompany;

public class CompanyFaker {

    private static final Faker FAKER = new Faker();

    public static CrmCompany createCompany() {
        return CrmCompany.builder()
                .name(FAKER.company().name())
                .address(FAKER.address().streetAddress())
                .build();
    }

    public static CompanyVO createCompanyVO() {
        return CompanyVO.builder()
                .name(FAKER.company().name())
                .address(FAKER.address().streetAddress())
                .build();
    }
}
