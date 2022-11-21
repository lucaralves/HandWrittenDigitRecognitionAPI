package com.example.digitrecognitionv3.dto;

public class DTO {

    private String base64;

    public DTO() {
    }

    public DTO(String base64) {
        this.base64 = base64;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
