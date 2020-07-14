package com.smartbee.crm.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbee.crm.base.ITBase;
import com.smartbee.crm.client.repo.CrmClient;
import com.smartbee.crm.company.repo.CrmCompany;
import com.smartbee.crm.faker.ClientFaker;
import com.smartbee.crm.faker.CompanyFaker;
import com.smartbee.crm.security.WithMockUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class ClientIT extends ITBase {

    private static final String URL = "/client";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper mapper;

    private CrmClient client;
    private CrmCompany company;

    @Before
    public void init() {
        super.init();
        company = createCompany(CompanyFaker.createCompany());
        client = ClientFaker.createClient(company.getId());
        createClient(client);
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void findClientByName() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("name", client.getName());
        final MvcResult result = this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        final String content = result.getResponse().getContentAsString();
        final CrmClient[] clients = mapper.readValue(content, CrmClient[].class);

        for (CrmClient client : clients) {
            Assert.assertEquals(this.client.getName(), client.getName());
        }
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void findClientById() throws Exception {
        final MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(URL + "/{id}", client.getId().toString());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(client.getName()))
                .andExpect(jsonPath("$.companyId").value(client.getCompanyId().toString()))
                .andExpect(jsonPath("$.email").value(client.getEmail()))
                .andExpect(jsonPath("$.phone").value(client.getPhone()));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void createClient() throws Exception {
        final CrmClient newClient = ClientFaker.createClient(company.getId());
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newClient));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newClient.getName()))
                .andExpect(jsonPath("$.companyId").value(newClient.getCompanyId().toString()))
                .andExpect(jsonPath("$.email").value(newClient.getEmail()))
                .andExpect(jsonPath("$.phone").value(newClient.getPhone()));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void updateClient() throws Exception {
        final String name = "Updated Name";
        final String phone = "456789101";
        final String email = "updated@gmail.com";

        client.setName(name);
        client.setPhone(phone);
        client.setEmail(email);

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(client));
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.phone").value(phone));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void deleteClient() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(URL + "/{id}", client.getId());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        final MockHttpServletRequestBuilder queryRequest =
                MockMvcRequestBuilders.get(URL + "/{id}", client.getId().toString());
        this.mockMvc.perform(queryRequest)
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(username = "manager", password = "manager")
    @Test
    public void createClientWithoutPrivilege() throws Exception {
        final CrmClient newClient = ClientFaker.createClient(company.getId());
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newClient));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
