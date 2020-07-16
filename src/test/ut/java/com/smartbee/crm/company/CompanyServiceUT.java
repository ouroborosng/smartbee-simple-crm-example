package com.smartbee.crm.company;

import com.smartbee.crm.auth.JwtUserDetailsService;
import com.smartbee.crm.company.repo.CompanyRepository;
import com.smartbee.crm.company.repo.CrmCompany;
import com.smartbee.crm.exception.DataNotFoundException;
import com.smartbee.crm.faker.CompanyFaker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@PowerMockIgnore( {"javax.management.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(PageRequest.class)
public class CompanyServiceUT {

    private CompanyService companyService;
    private JwtUserDetailsService mockUserDetailsService;
    private CompanyRepository mockCompanyRepository;

    public CompanyServiceUT() {
        mockUserDetailsService = mock(JwtUserDetailsService.class);
        mockCompanyRepository = mock(CompanyRepository.class);
        companyService = new CompanyService(mockUserDetailsService, mockCompanyRepository);
    }

    @Test
    public void testFindSingleCompanyByName() {
        final List<CrmCompany> companies = Arrays.asList(
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany()
        );
        final Optional<List<CrmCompany>> mockCompanies = Optional.of(companies);
        when(mockCompanyRepository.findByName(anyString())).thenReturn(mockCompanies);

        final List<CrmCompany> response = companyService.findCompanyByName("", null, null);

        verify(mockCompanyRepository).findByName(anyString());
        assertEquals(5, response.size());
    }

    @Test
    public void testPageableFindCompanyByName() {
        final int page = 0;
        final int size = 5;
        final List<CrmCompany> companies = Arrays.asList(
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany(),
                CompanyFaker.createCompany()
        );
        final PageImpl<CrmCompany> pages = new PageImpl(companies);
        PowerMockito.spy(PageRequest.of(page, size));
        when(mockCompanyRepository.findByName(anyString(), any())).thenReturn(pages);

        final List<CrmCompany> response = companyService.findCompanyByName("", page, size);

        verify(mockCompanyRepository).findByName(anyString(), any(PageRequest.class));
        assertEquals(5, response.size());
    }

    @Test
    public void testFindCompanyById() {
        final CrmCompany company = CompanyFaker.createCompany();
        when(mockCompanyRepository.findById(any(UUID.class))).thenReturn(Optional.of(company));

        companyService.findCompanyById(UUID.randomUUID());

        verify(mockCompanyRepository).findById(any(UUID.class));
    }

    @Test(expected = DataNotFoundException.class)
    public void testFindNotExistsCompanyId() {
        when(mockCompanyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        companyService.findCompanyById(UUID.randomUUID());
    }

    @Test
    public void testSaveCompany() {
        final UUID userId = UUID.randomUUID();
        final CrmCompany company = CompanyFaker.createCompany();
        final ArgumentCaptor<CrmCompany> companyCaptor = ArgumentCaptor.forClass(CrmCompany.class);
        when(mockUserDetailsService.getLoginUser()).thenReturn(userId.toString());
        when(mockCompanyRepository.save(any(CrmCompany.class))).thenReturn(company);

        companyService.saveCompany(company);

        verify(mockUserDetailsService).getLoginUser();
        verify(mockCompanyRepository).save(companyCaptor.capture());
        assertEquals(userId, companyCaptor.getValue().getCreatedBy());
        assertEquals(userId, companyCaptor.getValue().getUpdatedBy());
    }

    @Test
    public void testUpdateCompany() {
        final UUID userId = UUID.randomUUID();
        final UUID companyId = UUID.randomUUID();
        final CrmCompany updatedCompany = CompanyFaker.createCompany();
        updatedCompany.setId(companyId);
        final CrmCompany originCompany = CompanyFaker.createCompany();
        originCompany.setId(companyId);
        final ArgumentCaptor<CrmCompany> companyCaptor = ArgumentCaptor.forClass(CrmCompany.class);
        when(mockUserDetailsService.getLoginUser()).thenReturn(userId.toString());
        when(mockCompanyRepository.findById(any(UUID.class))).thenReturn(Optional.of(originCompany));
        when(mockCompanyRepository.save(any(CrmCompany.class))).thenReturn(updatedCompany);

        companyService.updateCompany(updatedCompany);

        verify(mockUserDetailsService).getLoginUser();
        verify(mockCompanyRepository).save(companyCaptor.capture());
        assertEquals(userId, companyCaptor.getValue().getUpdatedBy());
    }

    @Test(expected = DataNotFoundException.class)
    public void testUpdateNotExistsCompany() {
        final UUID userId = UUID.randomUUID();
        final CrmCompany company = CompanyFaker.createCompany();
        company.setId(UUID.randomUUID());
        when(mockUserDetailsService.getLoginUser()).thenReturn(userId.toString());
        when(mockCompanyRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        companyService.updateCompany(company);
    }

    @Test
    public void testDeleteCompany() {
        final UUID id = UUID.randomUUID();
        doNothing().when(mockCompanyRepository).deleteById(any(UUID.class));
        final ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);

        companyService.deleteCompany(id);

        verify(mockCompanyRepository).deleteById(idCaptor.capture());
        assertEquals(id, idCaptor.getValue());
    }

    @Test(expected = DataNotFoundException.class)
    public void testDeleteNotExistsCompany() {
        doThrow(new EmptyResultDataAccessException(1)).when(mockCompanyRepository).deleteById(any(UUID.class));

        companyService.deleteCompany(UUID.randomUUID());
    }
}
