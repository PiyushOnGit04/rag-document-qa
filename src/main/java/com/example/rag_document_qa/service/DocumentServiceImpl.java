package com.example.rag_document_qa.service;


import com.example.rag_document_qa.dto.DocumentResponse;
import com.example.rag_document_qa.entity.Document;
import com.example.rag_document_qa.enums.DocumentStatus;
import com.example.rag_document_qa.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository repository;

    private static final String UPLOAD_DIR = "uploads";

    @Override
    public DocumentResponse uploadDocument(MultipartFile file) {

        try {

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String uniqueFileName =
                    UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath =
                    Paths.get(UPLOAD_DIR, uniqueFileName);

            Files.copy(file.getInputStream(), filePath);

            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath.toString())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .status(DocumentStatus.UPLOADING)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            Document saved = repository.save(document);

            return DocumentResponse.builder()
                    .id(saved.getId())
                    .fileName(saved.getFileName())
                    .contentType(saved.getContentType())
                    .fileSize(saved.getFileSize())
                    .status(saved.getStatus())
                    .uploadedAt(saved.getUploadedAt())
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload document.", e);
        }
    }
}
