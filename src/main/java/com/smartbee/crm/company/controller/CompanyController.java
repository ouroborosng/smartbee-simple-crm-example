package com.smartbee.crm.company.controller;

import com.smartbee.crm.company.CompanyService;
import com.smartbee.crm.company.repo.CrmCompany;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(tags = "CRM Company API")
@Controller
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @ApiOperation("Find company by name with/without pagination")
    @GetMapping
    public ResponseEntity<List<CompanyVO>> findCompanyByName(
            @RequestParam(value = "name") String name,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        final List<CompanyVO> companies = companyService.findCompanyByName(name, page, size)
                .stream()
                .map(CompanyVO::create)
                .collect(Collectors.toList());
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @ApiOperation("Find company by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyVO> findCompanyById(@PathVariable(value = "id") UUID id) {
        return new ResponseEntity<>(
                CompanyVO.create(companyService.findCompanyById(id)),
                HttpStatus.OK
        );
    }

    @ApiOperation("Create a new company")
    @PostMapping
    public ResponseEntity<CompanyVO> createCompany(@RequestBody CompanyVO companyVO) {
        final CrmCompany company = CompanyVO.convert(companyVO);
        return new ResponseEntity<>(
                CompanyVO.create(companyService.saveCompany(company)),
                HttpStatus.CREATED
        );
    }

    @ApiOperation("Update existing company")
    @PutMapping
    public ResponseEntity<CompanyVO> updateCompany(@RequestBody CompanyVO companyVO) {
        final CrmCompany company = CompanyVO.convert(companyVO);
        return new ResponseEntity<>(
                CompanyVO.create(companyService.updateCompany(company)),
                HttpStatus.OK
        );
    }

    @ApiOperation("Delete company by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable(value = "id") UUID id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(id + " is deleted", HttpStatus.OK);
    }
}
