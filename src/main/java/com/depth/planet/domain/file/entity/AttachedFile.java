package com.depth.planet.domain.file.entity;

import com.depth.planet.common.auditor.UserStampedEntity;
import com.depth.planet.domain.file.entity.enums.FileType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public abstract class AttachedFile extends UserStampedEntity {
    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    private String fileName;

    private Long size;

    @Enumerated(EnumType.STRING)
    private FileType fileType;
}