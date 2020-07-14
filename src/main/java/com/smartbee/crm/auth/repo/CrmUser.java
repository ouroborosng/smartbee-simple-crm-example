package com.smartbee.crm.auth.repo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "crm_user")
public class CrmUser {

    @Id
    @Column(columnDefinition = "uuid", updatable = false )
    private UUID id;
    private String name;
    private String password;
    private String email;

    @ManyToOne(cascade={CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private CrmRole role;
}
