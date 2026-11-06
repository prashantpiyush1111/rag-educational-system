package com.education.rag.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FeedbackRequest(@NotNull Long queryLogId, @Min(1) @Max(5) int rating, @Size(max=2000) String comment) {}