package com.skillswap.service.storage;

import org.springframework.web.multipart.MultipartFile;

/**
 * Abstraction over "where uploaded files live". Two implementations:
 *  - LocalDiskStorageService (default, zero external dependencies, good for dev/demo)
 *  - CloudinaryStorageService (used in production, activated via app.storage.provider=cloudinary)
 *
 * Swapping providers is a config change only — callers depend on this interface, not on
 * Cloudinary or the filesystem directly.
 */
public interface StorageService {

    /**
     * Stores the file and returns a publicly resolvable URL (or local path) for it.
     */
    String store(MultipartFile file, String folder);
}
