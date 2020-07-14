package com.smartbee.crm.base.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class BaseEntity {

    @Id
    @Column(columnDefinition = "uuid", updatable = false )
    private UUID id;
    private UUID createdBy;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;
    private UUID updatedBy;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;
}
