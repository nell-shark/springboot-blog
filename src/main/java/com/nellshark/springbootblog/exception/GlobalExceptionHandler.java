package com.nellshark.springbootblog.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ArticleNotFoundException.class)
    public ModelAndView handleArticleNotFoundException(Exception exception) {
        log.warn(ArticleNotFoundException.class.getName() + " Occurred: " + exception.getMessage());
        return getModelAndView(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(Exception exception) {
        log.warn(UserNotFoundException.class.getName() + " Occurred: " + exception.getMessage());
        return getModelAndView(exception);
    }

    private ModelAndView getModelAndView(Exception e) {
        return new ModelAndView("error", "errorMessage", e.getMessage());
    }
}
