package com.example.rag_document_qa.service;

import com.example.rag_document_qa.dto.DocumentResponse;
import com.example.rag_document_qa.entity.Document;
import com.example.rag_document_qa.entity.DocumentChunk;
import com.example.rag_document_qa.enums.DocumentStatus;
import com.example.rag_document_qa.repository.DocumentChunkRepository;
import com.example.rag_document_qa.repository.DocumentRepository;
import dev.langchain4j.data.embedding.Embedding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final PdfExtractionService pdfExtractionService;
    private final DocumentRepository documentRepository;
    private final ChunkingService chunkingService;
    private final DocumentChunkRepository documentChunkRepository;
    private final EmbeddingService embeddingService;
    private static final String UPLOAD_DIR = "uploads";

    @Override
    @Transactional
    public DocumentResponse uploadDocument(MultipartFile file) {

        try {

            // Create uploads directory if it doesn't exist
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            // Generate unique file name
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Destination path
            Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);

            // Save PDF to uploads folder
            Files.copy(file.getInputStream(), filePath);

            // Extract text from PDF
            String extractedText = pdfExtractionService.extractText(filePath.toFile());

            // Save document metadata
            Document document = Document.builder()
                    .fileName(file.getOriginalFilename())
                    .filePath(filePath.toString())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .status(DocumentStatus.PROCESSING)
                    .uploadedAt(LocalDateTime.now())
                    .build();

            Document saved = documentRepository.save(document);

            List<String> chunks = chunkingService.chunkText(extractedText)
                    .stream()
                    .filter(c -> c != null && !c.isBlank())
                    .toList();

            if (chunks.isEmpty()) {
                saved.setStatus(DocumentStatus.FAILED); // or whatever status fits
                documentRepository.save(saved);
                throw new RuntimeException("No extractable text found in document.");
            }

            for (int i = 0; i < chunks.size(); i++) {
                Embedding embedding = embeddingService.generateEmbedding(chunks.get(i));

                DocumentChunk chunk = DocumentChunk.builder()
                        .chunkIndex(i)
                        .content(chunks.get(i))
                        .embedding(embedding.vector())
                        .document(saved)
                        .build();

                documentChunkRepository.save(chunk);
            }

            saved.setStatus(DocumentStatus.COMPLETED);
            documentRepository.save(saved);

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