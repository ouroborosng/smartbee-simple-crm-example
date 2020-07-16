package com.smartbee.crm.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbee.crm.base.ITBase;
import com.smartbee.crm.client.controller.ClientVO;
import com.smartbee.crm.company.controller.CompanyVO;
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

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class CompanyIT extends ITBase {
    private static final String URL = "/company";

    @Autowired
    private ObjectMapper mapper;

    private CrmCompany company;

    @Before
    public void init() {
        super.init();
        company = createCompany(CompanyFaker.createCompany());
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void findCompanyByName() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(URL)
                .param("name", company.getName());
        final MvcResult result = this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        final String content = result.getResponse().getContentAsString();
        final CompanyVO[] companies = mapper.readValue(content, CompanyVO[].class);

        for (final CompanyVO company : companies) {
            Assert.assertEquals(this.company.getName(), company.getName());
        }
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void findCompanyById() throws Exception {
        final MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.get(URL + "/{id}", company.getId().toString());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(company.getName()))
                .andExpect(jsonPath("$.address").value(company.getAddress()));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void createCompany() throws Exception {
        final CompanyVO newCompany = CompanyFaker.createCompanyVO();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCompany));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newCompany.getName()))
                .andExpect(jsonPath("$.address").value(newCompany.getAddress()));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void updateCompany() throws Exception {
        final String name = "Updated Name";
        final String address = "15F., No. 69, Sec. 3, Minsheng E. Rd., Zhongshan Dist., "
                + "Taipei City 104, Taiwan (R.O.C.)";
        final CompanyVO vo = CompanyVO.builder()
                .id(company.getId().toString())
                .name(name)
                .address(address)
                .build();

        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(vo));
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.address").value(address));
    }

    @WithMockUser(username = "admin", password = "admin")
    @Test
    public void deleteCompany() throws Exception {
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(URL + "/{id}", company.getId());
        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());

        final MockHttpServletRequestBuilder queryRequest =
                MockMvcRequestBuilders.get(URL + "/{id}", company.getId().toString());
        this.mockMvc.perform(queryRequest)
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @WithMockUser(username = "manager", password = "manager")
    @Test
    public void createCompanyWithoutPrivilege() throws Exception {
        final CompanyVO newCompany = CompanyFaker.createCompanyVO();
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(newCompany));

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
