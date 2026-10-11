package com.education.rag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record QueryRequest(
    @NotBlank(message="Question is required") @Size(max=8000,message="Question is too long") String question,
    @Min(value=1,message="topK must be at least 1") @Max(value=20,message="topK cannot exceed 20") Integer topK,
    @Size(max=128) String sessionId
) { public int effectiveTopK(){return topK == null ? 4 : topK;} }