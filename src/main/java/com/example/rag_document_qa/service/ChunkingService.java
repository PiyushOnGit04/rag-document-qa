package com.example.rag_document_qa.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChunkingService {

    private static final int CHUNK_SIZE = 500;

    public List<String> chunkText(String text) {

        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks; // empty list — nothing to chunk
        }

        for (int i = 0; i < text.length(); i += CHUNK_SIZE) {

            int end = Math.min(i + CHUNK_SIZE, text.length());

            chunks.add(text.substring(i, end));
        }

        return chunks.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}