package com.smartbee.crm.client.repo;

import com.smartbee.crm.base.pojo.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Entity
public class CrmClient extends BaseEntity {

    @Column(columnDefinition = "uuid", updatable = false)
    private UUID companyId;
    private String name;
    private String email;
    private String phone;
}
