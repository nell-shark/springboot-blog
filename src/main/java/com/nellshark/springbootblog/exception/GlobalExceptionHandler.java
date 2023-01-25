package com.nellshark.springbootblog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({
            ArticleNotFoundException.class,
            CommentNotFoundException.class,
            UserNotFoundException.class,
            FileIsEmptyException.class,
            FileIsNotImageException.class,
    })
    public ModelAndView handleAppException(Exception exception) {
        log.error(exception.getClass().getSimpleName() + " Occurred: " + exception.getMessage());
        return new ModelAndView("error", "exception", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(Exception exception) {
        log.error(exception.getClass().getSimpleName() + " Occurred: " + exception.getMessage());
        return new ModelAndView("users/sign-in");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    public ModelAndView handleIOException(Exception exception) {
        log.error(exception.getClass().getSimpleName() + " Occurred: " + exception.getMessage());
        return new ModelAndView("error", "exception", exception.getMessage());
    }
}
