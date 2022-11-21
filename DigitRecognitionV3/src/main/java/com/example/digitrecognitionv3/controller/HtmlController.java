package com.example.digitrecognitionv3.controller;

import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@org.springframework.stereotype.Controller
@RequestMapping("/api")
public class HtmlController {

    @RequestMapping(value = "/getWebPage",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public String getWebPage() {

        return "webpage";
    }
}
