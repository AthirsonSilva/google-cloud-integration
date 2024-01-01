package com.googlecloudintegration.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.googlecloudintegration.dto.InputFileDto;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class BucketService {

    @Value("${gcp.config.file}")
    private String gcpConfigFile;

    @Value("${gcp.project.id}")
    private String gcpProjectId;

    @Value("${gcp.bucket.id}")
    private String gcpBucketId;

    @Value("${gcp.dir.name}")
    private String gcpDirName;

    public InputFileDto uploadFile(MultipartFile file, String fileName, String contentType) {
        try {
            log.info("File uploaded: {}", file.getOriginalFilename());
            byte[] fileData = FileUtils.readFileToByteArray(convertMultiPartToFile(file));

            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();

            StorageOptions storageOptions = StorageOptions
                    .newBuilder()
                    .setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            Storage storage = storageOptions.getService();
            Bucket bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields());

            String uuid = String.valueOf(UUID.randomUUID());
            Blob blob = bucket.create(
                    gcpDirName + "/" + fileName + "-" + uuid + checkFileExtension(file.getOriginalFilename()),
                    fileData,
                    contentType);

            if (blob != null) {
                log.info("File uploaded to bucket: {}", blob.getName());
                return new InputFileDto(blob.getName(), blob.getMediaLink());
            }

            log.error("File upload failed");
            throw new RuntimeException("File upload failed");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private File convertMultiPartToFile(MultipartFile file) {
        try {
            if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
                throw new Exception("Invalid file name");
            }

            File convertedFile = new File(file.getOriginalFilename());
            convertedFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
            fos.close();

            log.info("File converted: {}", convertedFile.getName());
            return convertedFile;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String checkFileExtension(String fileName) {
        String[] extensionlist = { "jpg", "jpeg", "png", "gif", "bmp", "pdf", "doc", "docx", "xls", "xlsx", "ppt",
                "pptx", "txt", "csv", "xml", "zip", "rar", "tar", "gz", "7z" };

        for (String extension : extensionlist) {
            if (fileName.toLowerCase().endsWith(extension)) {
                log.info("File extension: {}", extension);
                return extension;
            }
        }

        log.error("Invalid file extension: {}", fileName);

        throw new RuntimeException("Invalid file extension");
    }

}
