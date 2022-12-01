package com.nellshark.springbootblog.utils;

import com.nellshark.springbootblog.config.WebConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static boolean saveMultipartFileToStorage(MultipartFile file, String folderName) {
        try {
            log.info("Save MultipartFile to the local storage: " + file.getOriginalFilename());
            String folderPath = WebConfig.STORAGE + File.separator + folderName;
            Files.createDirectories(Paths.get(folderPath));
            file.transferTo(new File(folderPath + File.separator + file.getOriginalFilename()));
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
