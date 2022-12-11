package com.nellshark.springbootblog.utils;

import com.nellshark.springbootblog.model.Article;
import com.nellshark.springbootblog.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static final String STORAGE_LOCATION = System.getProperty("user.dir") + File.separator
            + "storage" + File.separator;
    public static final String ARTICLES_STORAGE_LOCATION = STORAGE_LOCATION
            + "articles" + File.separator;
    private static final String USERS_STORAGE_LOCATION = STORAGE_LOCATION
            + "users" + File.separator;

    public static String saveUserAvatar(MultipartFile file, User user) throws IOException {
        log.info("Saving the User's Avatar: " + file.getOriginalFilename());
        String imageFolder = USERS_STORAGE_LOCATION + user.getId() + File.separator;

        user.getAvatar().ifPresent(avatar -> deleteFileIfExists(imageFolder + avatar));

        String filePath = imageFolder + file.getOriginalFilename();
        saveMultipartFile(file, filePath);

        return file.getOriginalFilename();
    }
    // TODO: CHANGE
//    public static boolean saveArticleImage(MultipartFile file, Long id){
//        log.info("Saving the Article's Image: " + file.getOriginalFilename());
//        String imageFolder = ARTICLES_STORAGE_LOCATION + id + File.separator;
//
//        deleteFileIfExists(imageFolder + article.getThumbnail());
//
//        String filePath = imageFolder + file.getOriginalFilename();
//
//        return saveMultipartFile(file, filePath);
//    }

    public static boolean saveArticleImage(MultipartFile file, Article article) throws IOException {
        log.info("Saving the Article's Image: " + file.getOriginalFilename());
        String imageFolder = ARTICLES_STORAGE_LOCATION + article.getId() + File.separator;

        deleteFileIfExists(imageFolder + article.getThumbnail());

        String filePath = imageFolder + file.getOriginalFilename();
        log.warn(filePath);

        return saveMultipartFile(file, filePath);
    }

    private static boolean saveMultipartFile(MultipartFile file, String filePath) throws IOException {
        log.info("Saving MultipartFile to the local storage: " + file.getOriginalFilename());
        deleteFileIfExists(filePath);

        Files.createDirectories(Paths.get(filePath));
        file.transferTo(new File(filePath));

        return true;
    }

    public static void deleteFileIfExists(String path) {
        log.info("Deleting file if exists: " + path);
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
