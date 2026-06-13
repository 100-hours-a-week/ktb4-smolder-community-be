package com.dragoncommunity.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
    String profileImageUpload(MultipartFile file);
}
