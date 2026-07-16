package com.example.rag_document_qa.service;

import com.example.rag_document_qa.dto.QueryResponse;
import com.example.rag_document_qa.entity.DocumentChunk;
import com.example.rag_document_qa.repository.DocumentChunkRepository;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryServiceImpl implements QueryService {

    private static final int TOP_K = 5;

    private final EmbeddingService embeddingService;
    private final DocumentChunkRepository documentChunkRepository;
    private final ChatModel chatModel; // LangChain4j chat model bean

    @Override
    public QueryResponse askQuestion(String question) {

        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question cannot be blank.");
        }

        // 1. Embed the question
        Embedding questionEmbedding = embeddingService.generateEmbedding(question);
        String vectorLiteral = toVectorLiteral(questionEmbedding.vector());

        // 2. Retrieve nearest chunks from pgvector
        List<DocumentChunk> nearestChunks =
                documentChunkRepository.findNearestChunks(vectorLiteral, TOP_K);

        if (nearestChunks.isEmpty()) {
            return QueryResponse.builder()
                    .answer("No relevant content found in the uploaded documents.")
                    .sourceChunks(List.of())
                    .build();
        }

        // 3. Build context from retrieved chunks
        String context = nearestChunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n---\n\n"));

        // 4. Build prompt and call the chat model
        String prompt = """
                Answer the question using only the context below.
                If the answer isn't in the context, say you don't know.

                Context:
                %s

                Question: %s
                """.formatted(context, question);

        String answer = chatModel.chat(prompt);

        return QueryResponse.builder()
                .answer(answer)
                .sourceChunks(nearestChunks.stream()
                        .map(c -> c.getDocument().getFileName() + " (chunk " + c.getChunkIndex() + ")")
                        .toList())
                .build();
    }

    private String toVectorLiteral(float[] vector) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < vector.length; i++) {
            sb.append(vector[i]);
            if (i < vector.length - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}
