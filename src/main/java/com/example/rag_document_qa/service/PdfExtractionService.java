package com.example.rag_document_qa.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PdfExtractionService {

    public String extractText(File file) {

        try (PDDocument document = Loader.loadPDF(file)) {

            PDFTextStripper stripper = new PDFTextStripper();

            String rawText = stripper.getText(document);

            return normalizeWhitespace(rawText);

        } catch (IOException e) {
            throw new RuntimeException("Failed to extract text from PDF: " + file.getName(), e);
        }
    }

    private String normalizeWhitespace(String text) {
        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("[ \\t]+", " ")      // collapse repeated spaces/tabs
                .replaceAll("\\n{3,}", "\n\n")   // collapse 3+ blank lines to one
                .trim();
    }
}