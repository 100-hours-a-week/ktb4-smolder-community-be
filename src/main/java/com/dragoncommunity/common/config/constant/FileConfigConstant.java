package com.dragoncommunity.common.config.constant;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class FileConfigConstant {

    public static final String RESOURCE_ALLOW_URL = "/public/**";

    public static final Path PROJECT_ROOT = Paths.get(System.getProperty("user.dir"));

    public static final Path UPLOAD_FOLDER = PROJECT_ROOT.resolve("src").resolve("main").resolve("resources").resolve("static").resolve("uploads");

    public static final Path PROFILE_DIR = UPLOAD_FOLDER.resolve("profile");

    public static final String PROFILE_URL = "/public/profile/";
    public static final String PROFILE_IMAGE_PREFIX = "profile";

    public static final List<String> ALLOWED_IMAGE_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");

    public static final long MAX_PROFILE_SIZE = 10 * 1024 * 1024;

    private FileConfigConstant() {
    }

}

