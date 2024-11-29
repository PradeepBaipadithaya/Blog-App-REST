package com.springboot.blog.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ImageValidationResponse {
    @JsonProperty("Result")
    private String result;
    @JsonProperty("Confidence")// "Result" field from API response
    private String confidence;
    @JsonProperty("Reasoning")// "Confidence" field from API response (optional)
    private String reasoning;       // "Reasoning" field from API response
}
