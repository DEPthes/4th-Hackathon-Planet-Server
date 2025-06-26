package com.depth.planet.domain.file.service;

import com.depth.planet.domain.file.entity.AttachedFile;
import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.file.repository.AttachedFileRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AttachedFileService {
    private final AttachedFileRepository attachedFileRepository;
    private final FileSystemHandler fileSystemHandler;

    @Transactional(readOnly = true)
    public Resource getResourceById(String fileId) {
        AttachedFile found = attachedFileRepository.findById(fileId)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        return fileSystemHandler.loadFileAsResource(found);
    }
}
