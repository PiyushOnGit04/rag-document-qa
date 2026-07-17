package com.example.rag_document_qa.service;

import com.example.rag_document_qa.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {


    DocumentResponse uploadDocument(MultipartFile file);

    void deleteDocument(long id);

    List<DocumentResponse> getAllDocuments();

}
