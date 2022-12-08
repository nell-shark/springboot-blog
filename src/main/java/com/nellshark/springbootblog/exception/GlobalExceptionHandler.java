package com.nellshark.springbootblog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArticleNotFoundException.class)
    public ModelAndView handleArticleNotFoundException(Exception exception) {
        log.warn(ArticleNotFoundException.class.getName() + " Occurred: " + exception.getMessage());
        return getModelAndView(Map.of("exception", exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(Exception exception) {
        log.warn(UserNotFoundException.class.getName() + " Occurred: " + exception.getMessage());
        return getModelAndView(Map.of("exception", exception.getMessage()));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(Exception exception) {
        log.warn(AccessDeniedException.class.getName() + " Occurred: " + exception.getMessage());
        return getModelAndView(Map.of("exception", exception.getMessage()));
    }

    private ModelAndView getModelAndView(Map<String, Object> model) {
        return new ModelAndView("error", model);
    }
}
