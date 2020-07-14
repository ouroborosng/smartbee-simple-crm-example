package com.smartbee.crm.auth.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crm_role")
public class CrmRole {

    @Id
    @Column(columnDefinition = "uuid", updatable = false )
    private UUID id;
    private String name;

    @OneToMany(mappedBy = "roleId", cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<CrmPermission> permissions = new HashSet<>();
}
