package com.depth.planet.domain.file.entity;

import com.depth.planet.domain.file.entity.enums.FileType;
import com.depth.planet.domain.quest.entity.Quest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("EVIDENCE_IMAGE")
public class EvidenceImage extends AttachedFile{
    @OneToOne(fetch = FetchType.LAZY)
    private Quest quest;

    public static EvidenceImage from(MultipartFile file, Quest quest) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return EvidenceImage.builder()
                .id(UUID.randomUUID().toString())
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .fileType(FileType.IMAGE)
                .quest(quest)
                .build();
    }
}
