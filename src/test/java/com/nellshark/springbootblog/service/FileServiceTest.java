package com.nellshark.springbootblog.service;

import com.nellshark.springbootblog.exception.FileIsEmptyException;
import com.nellshark.springbootblog.exception.FileIsNotImageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class FileServiceTest {
    private FileService fileService;

    @BeforeEach
    void setUp() {
        fileService = new FileService();
    }

    @Test
    void should_throwFileIsEmptyException_when_fileIsNull() {
        assertThrows(FileIsEmptyException.class,
                () -> fileService.saveMultipartFileToLocalStorage(null, "fileFolder"));
    }

    @Test
    void should_throwFileIsEmptyException_when_fileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile("name",
                "image.png",
                "image/png",
                "".getBytes());
        assertThrows(FileIsEmptyException.class,
                () -> fileService.saveMultipartFileToLocalStorage(file, "fileFolder"));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "application/java-archive",
            "application/EDI-X12",
            "application/EDIFACT",
            "application/javascript",
            "application/octet-stream",
            "application/ogg",
            "application/pdf",
            "application/xhtml+xml",
            "application/x-shockwave-flash",
            "application/json",
            "application/ld+json",
            "application/xml",
            "application/zip",
            "application/x-www-form-urlencoded",
            "application/java-archive",
            "application/EDI-X12",
            "application/EDIFACT",
            "application/javascript",
            "application/octet-stream",
            "application/ogg",
            "application/pdf",
            "application/xhtml+xml",
            "application/x-shockwave-flash",
            "application/json",
            "application/ld+json",
            "application/xml",
            "application/zip",
            "application/x-www-form-urlencoded",
            "text/css",
            "text/csv",
            "text/html",
            "text/javascript(obsolete)",
            "text/plain",
            "text/xml",
            "text/css",
            "text/csv",
            "text/html",
            "text/javascript(obsolete)",
            "text/plain",
            "text/xml",
            "image/gif",
            "image/vnd.microsoft.icon",
            "image/x-icon",
            "image/vnd.djvu"
    })
    void should_throwFileIsNotImageException_when_fileContentTypeIsNotSuitable(String contentType) {
        MockMultipartFile file = new MockMultipartFile("multipartFile",
                "name",
                contentType,
                "content".getBytes());

        assertThrows(FileIsNotImageException.class,
                () -> fileService.saveMultipartFileToLocalStorage(file, "fileFolder"));
    }
}