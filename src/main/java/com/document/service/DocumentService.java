package com.document.service;

import com.document.dto.DocumentDTO;
import com.document.entity.Document;
import com.document.mapper.ModelMapper;
import com.document.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentService.class);
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AWSS3FileUploadService awss3FileUploadService;

    public DocumentDTO saveDocuments(DocumentDTO documentDTO, MultipartFile file ) throws Exception {
        try {
            log.info("Saving Document");
            Document document = modelMapper.toEntity(documentDTO);
            documentRepository.save(document);
            if (file != null){
                String url = awss3FileUploadService.fileUpload(file);
                document.setFileURL(url);
                documentRepository.save(document);
            }
            return modelMapper.toDTO(document);
        } catch (Exception ex) {
            log.info("Error while save Document",ex);
            throw new Exception("Exception while uploading the doc",ex);
        }
    }

    public List<Document> getAllDocuments() {
        log.info("Get all document request");
        return documentRepository.findAll();
    }

    public Document getByDocumentId(Long documentId) {
        log.info("Get document by id: {}",documentId);
        return documentRepository.findById(documentId).orElseThrow(null);
    }
}
