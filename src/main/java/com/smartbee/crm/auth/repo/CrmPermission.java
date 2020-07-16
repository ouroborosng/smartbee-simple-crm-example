package com.smartbee.crm.auth.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crm_permission")
public class CrmPermission implements GrantedAuthority {

    @Id
    @Column(columnDefinition = "uuid", updatable = false )
    private UUID id;
    private UUID roleId;
    private String authority;
}
