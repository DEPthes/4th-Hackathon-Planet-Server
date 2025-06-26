package com.depth.planet.domain.file.dto;

import java.util.Objects;

import com.depth.planet.domain.file.entity.AttachedFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class FileDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "파일 응답 DTO")
    public static class FileResponse {
        @Schema(description = "파일 ID (UUID)", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        private String id;
        @Schema(description = "원본 파일 이름", example = "image.jpg")
        private String fileName;
        @Schema(description = "파일 크기 (bytes)", example = "102400")
        private Long size;

        public static FileResponse from(AttachedFile file) {
            if (Objects.isNull(file)) {
                return null;
            }

            return FileResponse.builder()
                    .id(file.getId())
                    .fileName(file.getFileName())
                    .size(file.getSize())
                    .build();
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    @Schema(description = "Base64 이미지 응답 DTO")
    public static class Base64ImageResponse {
        @Schema(description = "파일 ID (UUID)", example = "a1b2c3d4-e5f6-7890-1234-567890abcdef")
        private String id;
        @Schema(description = "원본 파일 이름", example = "image.jpg")
        private String fileName;
        @Schema(description = "파일 크기 (bytes)", example = "102400")
        private Long size;
        @Schema(description = "Base64로 인코딩된 이미지 데이터", example = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD...")
        private String base64Data;
        @Schema(description = "이미지 MIME 타입", example = "image/jpeg")
        private String mimeType;

        public static Base64ImageResponse from(AttachedFile file, String base64Data, String mimeType) {
            if (Objects.isNull(file)) {
                return null;
            }

            return Base64ImageResponse.builder()
                    .id(file.getId())
                    .fileName(file.getFileName())
                    .size(file.getSize())
                    .base64Data(base64Data)
                    .mimeType(mimeType)
                    .build();
        }
    }
}
