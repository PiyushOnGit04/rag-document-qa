package com.example.rag_document_qa.service;

import dev.langchain4j.data.embedding.Embedding;

public interface EmbeddingService {

    Embedding generateEmbedding(String text);

}
