package com.example.rag_document_qa.dto;

import com.example.rag_document_qa.enums.DocumentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResponse {

    private Long id;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private DocumentStatus status;
    private LocalDateTime uploadedAt;

}
