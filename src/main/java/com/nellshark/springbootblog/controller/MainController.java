package com.nellshark.springbootblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class MainController {
    @GetMapping
    public ModelAndView redirectToIndexPage() throws IOException {
        return new ModelAndView("redirect:/articles");
    }
}
