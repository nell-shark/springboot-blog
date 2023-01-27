package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.FileIsEmptyException;
import com.nellshark.springbootblog.exception.FileIsNotImageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.apache.commons.io.FilenameUtils.EXTENSION_SEPARATOR;
import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.http.entity.ContentType.IMAGE_JPEG;
import static org.apache.http.entity.ContentType.IMAGE_PNG;
import static org.apache.http.entity.ContentType.IMAGE_WEBP;

@Service
@Slf4j
public class FileService {
    public static final String STORAGE_LOCATION = System.getProperty("user.dir").replace("\\", "/")
            + "/storage";

    public String saveMultipartFileToLocalStorage(MultipartFile file, String fileFolder) throws IOException {
        log.info("Saving Image to the local storage: " + file);
        isEmpty(file);
        isImage(file);

        String fileName = generateFileName()
                + EXTENSION_SEPARATOR
                + getExtension(file.getOriginalFilename());

        Path path = Path.of(STORAGE_LOCATION
                + fileFolder + "/"
                + fileName);

        try {
            Files.deleteIfExists(path);
            Files.createDirectories(path.getParent());
            file.transferTo(path);
        } catch (IOException e) {
            throw new IOException("File cannot be saved");
        }

        return fileName;
    }

    private void isEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) throw new FileIsEmptyException("Cannot save the empty file");
    }

    private void isImage(MultipartFile file) {
        if (!Set.of(IMAGE_JPEG.getMimeType(),
                        IMAGE_PNG.getMimeType(),
                        IMAGE_WEBP.getMimeType())
                .contains(file.getContentType()))
            throw new FileIsNotImageException("File must be an image: " + file.getContentType());
    }

    private String generateFileName() {
        log.info("Generating a file name");
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS"));
    }
}
