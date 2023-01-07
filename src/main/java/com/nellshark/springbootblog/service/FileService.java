package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.FileIsEmptyException;
import com.nellshark.springbootblog.exception.FileIsNotImageException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;
import static org.apache.http.entity.ContentType.IMAGE_WEBP;

@Service
@Slf4j
public class FileService {
    public static final String APP_LOCATION = System.getProperty("user.dir").replace("\\", "/");
    public static final String STORAGE_FOLDER = "/storage";

    public void saveMultipartFile(MultipartFile file, String filePath) throws IOException {
        log.info("Saving Image to the local storage: " + file.getOriginalFilename());
        if (file.isEmpty()) throw new FileIsEmptyException("Cannot save empty file");
        if (Set.of(IMAGE_JPEG.getMimeType(),
                        IMAGE_PNG.getMimeType(),
                        IMAGE_WEBP.getMimeType())
                .contains(file.getContentType()))
            throw new FileIsNotImageException("File must be an image: " + file.getContentType());

        Path path = Path.of(filePath);
        try {
            Files.deleteIfExists(path);
            Files.createDirectories(path);
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new IOException("File cannot be saved");
        }
    }

    public String getNewFileName(String oldName) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS"))
                + FilenameUtils.EXTENSION_SEPARATOR
                + FilenameUtils.getExtension(oldName);
    }
}
