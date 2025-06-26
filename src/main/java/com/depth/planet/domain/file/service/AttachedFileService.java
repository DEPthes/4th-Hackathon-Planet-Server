package com.depth.planet.domain.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.depth.planet.domain.file.dto.FileDto;
import com.depth.planet.domain.file.entity.AttachedFile;
import com.depth.planet.domain.file.handler.FileSystemHandler;
import com.depth.planet.domain.file.repository.AttachedFileRepository;
import com.depth.planet.system.exception.model.ErrorCode;
import com.depth.planet.system.exception.model.RestException;

import lombok.RequiredArgsConstructor;

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

    @Transactional(readOnly = true)
    public FileDto.Base64ImageResponse getBase64ImageById(String fileId) {
        AttachedFile found = attachedFileRepository.findById(fileId)
                .orElseThrow(() -> new RestException(ErrorCode.GLOBAL_NOT_FOUND));

        try {
            Resource resource = fileSystemHandler.loadFileAsResource(found);
            Path filePath = resource.getFile().toPath();

            // 파일을 바이트 배열로 읽기
            byte[] fileBytes = Files.readAllBytes(filePath);

            // Base64로 인코딩
            String base64Data = Base64.getEncoder().encodeToString(fileBytes);

            // MIME 타입 결정
            String mimeType = determineMimeType(found.getFileName());

            // Data URL 형식으로 구성
            String dataUrl = "data:" + mimeType + ";base64," + base64Data;

            return FileDto.Base64ImageResponse.from(found, dataUrl, mimeType);

        } catch (IOException e) {
            throw new RestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String determineMimeType(String fileName) {
        if (fileName == null) {
            return "application/octet-stream";
        }

        String lowerCaseFileName = fileName.toLowerCase();

        if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (lowerCaseFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerCaseFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerCaseFileName.endsWith(".webp")) {
            return "image/webp";
        } else if (lowerCaseFileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerCaseFileName.endsWith(".svg")) {
            return "image/svg+xml";
        } else {
            return "application/octet-stream";
        }
    }
}
