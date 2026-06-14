package com.dragoncommunity.infrastructure.storage;

import com.dragoncommunity.common.exception.ApplicationException;
import com.dragoncommunity.common.util.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.dragoncommunity.common.config.constant.FileConfigConstant.*;
import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.FILE_NOT_EXISTS;
import static com.dragoncommunity.common.exception.enums.ApplicationErrorCode.FILE_UPLOAD_FAILED;

@Component
public class LocalFileManager implements FileManager {

    /**
     * 로컬에 파일을 저장한다.
     */
    @Override
    public String profileImageUpload(MultipartFile file){

        String filename = generateFilename(file,PROFILE_IMAGE_PREFIX);

        Path savePath = PROFILE_DIR.resolve(filename);

        try {
            if (!Files.exists(PROFILE_DIR)) {
                Files.createDirectories(PROFILE_DIR);
            }
            file.transferTo(savePath.toFile());
        } catch (IOException exception) {
            throw new ApplicationException(FILE_UPLOAD_FAILED);
        }

        return PROFILE_URL + filename;
    }

    /**
     * 프로필 이미지가 Storage에 있는지 확인한다.
     */
    @Override
    public void validateProfileImageExists(String profileImageUrl) {

        String fileName = FileUtil.extractFileNameFromPath(profileImageUrl,PROFILE_URL);

        Path filePath = PROFILE_DIR.resolve(fileName);

        if (!Files.isRegularFile(filePath)) {
            throw new ApplicationException(FILE_NOT_EXISTS);
        }
    }

    /**
     * 파일 이름을 생성한다.
     */
    private String generateFilename(MultipartFile file,String prefix) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        String uuid = UUID.randomUUID().toString();

        return prefix + "-" + timestamp + "-" + uuid + "." + extension;
    }
}
