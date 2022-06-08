package com.nellshark.springbootblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Article {
    private String title;
    private String text;
    private LocalDate date;
    private String description;
}
