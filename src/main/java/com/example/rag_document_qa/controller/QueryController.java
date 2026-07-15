package com.example.rag_document_qa.controller;

import com.example.rag_document_qa.dto.QueryResponse;
import com.example.rag_document_qa.service.QueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/query")
@RequiredArgsConstructor
public class QueryController {

    private final QueryService queryService;

    @PostMapping
    public QueryResponse query(@RequestBody QueryRequest request) {
        return queryService.askQuestion(request.getQuestion());
    }

    // simple inline request DTO — move to its own file if you prefer
    @lombok.Getter
    @lombok.Setter
    public static class QueryRequest {
        private String question;
    }
}