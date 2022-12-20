package com.nellshark.springbootblog.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {
    public static final String APP_LOCATION = System.getProperty("user.dir").replace("\\", "/");
    public static final String STORAGE_FOLDER = "/storage";

    public static void saveMultipartFile(MultipartFile file, String filePath) throws IOException {
        log.info("Saving MultipartFile to the local storage: " + file.getOriginalFilename());

        Path path = Path.of(filePath);
        Files.deleteIfExists(path);
        Files.createDirectories(path);
        file.transferTo(new File(filePath));
    }

    public static String getNewFileName(String oldName) {
        return String.valueOf(System.currentTimeMillis())
                + FilenameUtils.EXTENSION_SEPARATOR
                + FilenameUtils.getExtension(oldName);
    }
}
