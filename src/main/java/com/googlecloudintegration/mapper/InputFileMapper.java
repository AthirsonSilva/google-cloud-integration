package com.googlecloudintegration.mapper;

import com.googlecloudintegration.dto.InputFileDto;
import com.googlecloudintegration.entity.InputFileEntity;

public class InputFileMapper {

    public static InputFileDto toDto(InputFileEntity entity) {
        return new InputFileDto(entity.getFileName(), entity.getFileUri());
    }

    public static InputFileEntity toEntity(InputFileDto dto) {
        InputFileEntity entity = new InputFileEntity();
        entity.setFileName(dto.getFileName());
        entity.setFileUri(dto.getFileUri());
        return entity;
    }

}
