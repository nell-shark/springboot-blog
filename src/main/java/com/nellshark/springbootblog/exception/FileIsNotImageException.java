package com.nellshark.springbootblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class FileIsNotImageException extends RuntimeException {
    public FileIsNotImageException(String message) {
        super(message);
    }
}
