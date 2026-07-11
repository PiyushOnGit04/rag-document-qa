package com.example.rag_document_qa.repository;


import com.example.rag_document_qa.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    boolean existsByFileName(String fileName);
}
