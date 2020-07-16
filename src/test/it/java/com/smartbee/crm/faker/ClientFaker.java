package com.smartbee.crm.faker;

import com.github.javafaker.Faker;
import com.smartbee.crm.client.controller.ClientVO;
import com.smartbee.crm.client.repo.CrmClient;

import java.util.UUID;

public class ClientFaker {

    private static final Faker FAKER = new Faker();

    public static CrmClient createClient(final UUID companyId) {
        return CrmClient.builder()
                .companyId(companyId)
                .name(FAKER.name().username())
                .email(FAKER.internet().safeEmailAddress())
                .phone(FAKER.phoneNumber().cellPhone())
                .build();
    }

    public static ClientVO createClientVO(final String companyId) {
        return ClientVO.builder()
                .companyId(companyId)
                .name(FAKER.name().username())
                .email(FAKER.internet().safeEmailAddress())
                .phone(FAKER.phoneNumber().cellPhone())
                .build();
    }
}
