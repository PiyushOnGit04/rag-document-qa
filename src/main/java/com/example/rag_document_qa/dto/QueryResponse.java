package com.example.rag_document_qa.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QueryResponse {
    private String answer;
    private List<String> sourceChunks;
}
