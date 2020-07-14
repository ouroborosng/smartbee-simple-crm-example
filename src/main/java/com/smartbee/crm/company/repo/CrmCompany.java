package com.smartbee.crm.company.repo;

import com.smartbee.crm.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class CrmCompany extends BaseEntity {

    private String name;
    private String address;
}
