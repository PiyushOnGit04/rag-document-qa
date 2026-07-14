package com.example.rag_document_qa.repository;

import com.example.rag_document_qa.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentChunkRepository
        extends JpaRepository<DocumentChunk, Long> {

    @Query(value =
            "SELECT * FROM document_chunks " +
                    "ORDER BY embedding <=> CAST(:queryVector AS vector) " +
                    "LIMIT :topK",
            nativeQuery = true)
    List<DocumentChunk> findNearestChunks(
            @Param("queryVector") String queryVector,
            @Param("topK") int topK
    );
}
