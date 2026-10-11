package com.education.rag.dto.response;

import java.util.List;

public record QueryResponse(String answer, List<String> sources, String question) {}