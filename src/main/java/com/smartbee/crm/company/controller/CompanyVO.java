package com.smartbee.crm.company.controller;

import com.smartbee.crm.base.pojo.BaseVO;
import com.smartbee.crm.company.repo.CrmCompany;
import com.smartbee.crm.exception.InvalidUUIDException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@ApiModel("Company Model")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CompanyVO extends BaseVO {

    @ApiModelProperty(required = true)
    private String name;
    private String address;

    public static CompanyVO create(final CrmCompany company) {
        return CompanyVO.builder()
                .id(company.getId().toString())
                .name(company.getName())
                .address(company.getAddress())
                .build();
    }

    public static CrmCompany convert(final CompanyVO vo) {
        try {
            final UUID id = StringUtils.isBlank(vo.getId()) ? new UUID(0, 0) : UUID.fromString(vo.getId());
            return CrmCompany.builder()
                    .id(id)
                    .name(vo.getName())
                    .address(vo.getAddress())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(e.getMessage());
        }
    }
}
