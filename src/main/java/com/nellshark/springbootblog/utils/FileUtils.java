package com.nellshark.springbootblog.utils;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static final String STORAGE_LOCATION = System.getProperty("user.dir") + File.separator
            + "storage" + File.separator;

    private static final String ARTICLES_STORAGE_LOCATION = STORAGE_LOCATION + "articles" + File.separator;
    private static final String USERS_STORAGE_LOCATION = STORAGE_LOCATION + "users" + File.separator;

    public static void saveMultipartFile(MultipartFile file, String filePath) {
        try {
            log.info("Save MultipartFile to the local storage: " + file.getOriginalFilename());
            Files.createDirectories(Paths.get(filePath));
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static String saveUserAvatar(MultipartFile file, User user) {
        String imageFolder = USERS_STORAGE_LOCATION + user.getId() + File.separator;

        if (user.getImage().isPresent()) deleteFile(imageFolder + user.getImage().get());

        String filePath = imageFolder + file.getOriginalFilename();
        saveMultipartFile(file, filePath);

        return file.getOriginalFilename();
    }

    public static String saveArticleThumbnail(MultipartFile file, Article article) {
        String imageFolder = ARTICLES_STORAGE_LOCATION + article.getId() + File.separator;

        if (!StringUtils.isEmpty(article.getImage())) deleteFile(imageFolder + article.getImage());

        String filePath = imageFolder + file.getOriginalFilename();
        saveMultipartFile(file, filePath);

        return file.getOriginalFilename();
    }

    public static void deleteFile(String path) {
        log.info("Deleting file: " + path);
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }
}
