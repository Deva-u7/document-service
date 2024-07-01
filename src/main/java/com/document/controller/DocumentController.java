package com.document.controller;

import com.document.dto.DocumentDTO;
import com.document.entity.Document;
import com.document.service.DocumentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DocumentDTO> saveDocuments(@RequestParam String documentDTO,
                                                     @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        DocumentDTO request = objectMapper.readValue(documentDTO, DocumentDTO.class);
        DocumentDTO response = documentService.saveDocuments(request, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() throws InterruptedException {
        List<Document> document = documentService.getAllDocuments();
        return ResponseEntity.ok(document);
    }

    @GetMapping("/{document-id}")
    public ResponseEntity<Document> getByDocumentId(@PathVariable("document-id") Long documentId) {
        Document document = documentService.getByDocumentId(documentId);
        return ResponseEntity.ok(document);
    }
}
