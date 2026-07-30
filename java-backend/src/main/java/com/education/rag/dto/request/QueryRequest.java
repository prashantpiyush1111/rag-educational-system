package com.education.rag.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record QueryRequest(

        @NotBlank(message = "Question must not be empty")
        @Size(max = 1000, message = "Question must not exceed 1000 characters")
        String question,

        @Min(value = 1, message = "top_k must be at least 1")
        @Max(value = 10, message = "top_k must not exceed 10")
        Integer topK
) {
    // Default top_k if frontend doesn't send it
    public Integer topK() {
        return topK == null ? 4 : topK;
    }
}