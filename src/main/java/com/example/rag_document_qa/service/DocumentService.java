package com.example.rag_document_qa.service;

import com.example.rag_document_qa.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {


    DocumentResponse uploadDocument(MultipartFile file);

}
