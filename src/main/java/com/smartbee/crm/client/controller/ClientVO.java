package com.smartbee.crm.client.controller;

import com.smartbee.crm.base.pojo.BaseVO;
import com.smartbee.crm.client.repo.CrmClient;
import com.smartbee.crm.exception.InvalidUUIDException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@ApiModel("Client Model")
@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ClientVO extends BaseVO {

    @ApiModelProperty(required = true)
    private String companyId;
    @ApiModelProperty(required = true)
    private String name;
    private String email;
    private String phone;

    public static ClientVO create(final CrmClient client) {
        return ClientVO.builder()
                .id(client.getId().toString())
                .companyId(client.getCompanyId().toString())
                .name(client.getName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .build();
    }

    public static CrmClient convert(final ClientVO vo) {
        try {
            final UUID id = StringUtils.isBlank(vo.getId()) ? new UUID(0, 0) : UUID.fromString(vo.getId());
            final UUID companyId = StringUtils.isBlank(vo.getCompanyId()) ? new UUID(0, 0) : UUID.fromString(vo.getCompanyId());
            return CrmClient.builder()
                    .id(id)
                    .companyId(companyId)
                    .name(vo.getName())
                    .email(vo.getEmail())
                    .phone(vo.getPhone())
                    .build();
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDException(e.getMessage());
        }
    }
}
