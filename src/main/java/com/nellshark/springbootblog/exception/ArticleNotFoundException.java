package com.nellshark.springbootblog.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }
}
