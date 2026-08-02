package com.skillswap.service.storage;

import com.skillswap.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

/**
 * Default storage provider. Writes files under a local directory and serves
 * them back via a static resource mapping (see WebConfig). Good enough for
 * local dev / demos; swap to Cloudinary for a real deployment by setting
 * app.storage.provider=cloudinary (see CloudinaryStorageService).
 */
@Service
@ConditionalOnProperty(name = "app.storage.provider", havingValue = "local", matchIfMissing = true)
public class LocalDiskStorageService implements StorageService {

    private final Path rootDir;
    private final String publicBaseUrl;

    public LocalDiskStorageService(
            @Value("${app.storage.local.root-dir:uploads}") String rootDir,
            @Value("${app.storage.local.public-base-url:http://localhost:8080/files}") String publicBaseUrl) {
        this.rootDir = Path.of(rootDir);
        this.publicBaseUrl = publicBaseUrl;
    }

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            throw new ApiException("File is empty", HttpStatus.BAD_REQUEST);
        }

        String originalName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "file");
        String extension = originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : "";
        String safeName = UUID.randomUUID() + extension;

        try {
            Path targetDir = rootDir.resolve(folder);
            Files.createDirectories(targetDir);

            Path targetPath = targetDir.resolve(safeName).normalize();
            if (!targetPath.startsWith(targetDir)) {
                // Defends against path traversal via a crafted filename.
                throw new ApiException("Invalid file name", HttpStatus.BAD_REQUEST);
            }

            file.transferTo(targetPath);
            return publicBaseUrl + "/" + folder + "/" + safeName;
        } catch (IOException e) {
            throw new ApiException("Failed to store file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
