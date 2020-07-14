package com.smartbee.crm.base;

import com.smartbee.crm.client.ClientService;
import com.smartbee.crm.client.repo.CrmClient;
import com.smartbee.crm.company.CompanyService;
import com.smartbee.crm.company.repo.CrmCompany;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ITBase {

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ClientService clientService;

    protected MockMvc mockMvc;

    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    protected CrmCompany createCompany(final CrmCompany company) {
        return companyService.saveCompany(company);
    }

    protected CrmClient createClient(final CrmClient client) {
        return clientService.saveClient(client);
    }
}
