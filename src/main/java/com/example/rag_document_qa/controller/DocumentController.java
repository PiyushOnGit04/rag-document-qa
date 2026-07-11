package com.example.rag_document_qa.controller;

import com.example.rag_document_qa.dto.DocumentResponse;
import com.example.rag_document_qa.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentResponse> upload(
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(documentService.uploadDocument(file));
    }

}
