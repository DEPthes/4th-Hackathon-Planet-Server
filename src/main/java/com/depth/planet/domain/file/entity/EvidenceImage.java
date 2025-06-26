package com.depth.planet.domain.file.entity;

import com.depth.planet.domain.file.entity.enums.FileType;
import com.depth.planet.domain.quest.entity.Quest;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("EVIDENCE_IMAGE")
public class EvidenceImage extends AttachedFile{
    @ManyToOne(fetch = FetchType.LAZY)
    private Quest quest;

    public static EvidenceImage from(MultipartFile file, Quest quest) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        return EvidenceImage.builder()
                .id(file.getOriginalFilename()) // Assuming the ID is the filename for simplicity
                .fileName(file.getOriginalFilename())
                .size(file.getSize())
                .fileType(FileType.IMAGE)
                .quest(quest)
                .build();
    }
}
