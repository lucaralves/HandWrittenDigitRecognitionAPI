package com.example.digitrecognitionv3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class DigitRecognitionV3Application extends SpringBootServletInitializer {


    /*
    public static void main(String[] args) {
        SpringApplication.run(DigitRecognitionV3Application.class, args);
    }
     */

    public static void main(String[] args) {
        new SpringApplicationBuilder(DigitRecognitionV3Application.class)
                .web(WebApplicationType.NONE) // .REACTIVE, .SERVLET
                .run(args);
    }
}
