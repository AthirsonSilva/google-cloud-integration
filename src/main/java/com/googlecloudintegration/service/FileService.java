package com.googlecloudintegration.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.services.storage.model.Bucket;
import com.googlecloudintegration.dto.InputFileDto;
import com.googlecloudintegration.entity.InputFileEntity;
import com.googlecloudintegration.mapper.InputFileMapper;
import com.googlecloudintegration.repository.FileRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class FileService {

    private final FileRepository fileRepository;
    private final BucketService bucketService;

    public List<InputFileDto> saveFile(MultipartFile[] files) {
        log.info("Files received: {}", files.length);
        ArrayList<InputFileEntity> inputFileDtos = new ArrayList<>();

        List.of(files).forEach(file -> {
            String originalFileName = file.getOriginalFilename();

            if (originalFileName == null) {
                throw new RuntimeException("File name is null");
            }

            Path path = new File(originalFileName).toPath();

            try {
                String contentType = Files.probeContentType(path);
                InputFileDto inputFileDto = bucketService.uploadFile(file, originalFileName, contentType);

                if (inputFileDto != null) {
                    InputFileEntity inputFileEntity = new InputFileEntity();
                    inputFileEntity.setFileName(inputFileDto.getFileName());
                    inputFileEntity.setFileUri(inputFileDto.getFileUri());
                    inputFileDtos.add(inputFileEntity);
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        });

        List<InputFileEntity> savedFiles = fileRepository.saveAll(inputFileDtos);
        log.info("Files saved: {}", savedFiles.size());
        List<InputFileDto> convertedFiles = savedFiles.stream().map(InputFileMapper::toDto).toList();
        return convertedFiles;
    }

}
