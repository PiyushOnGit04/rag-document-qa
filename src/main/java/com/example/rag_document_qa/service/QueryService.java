package com.example.rag_document_qa.service;


import com.example.rag_document_qa.dto.QueryResponse;

public interface QueryService {
    QueryResponse askQuestion(String question);
}
