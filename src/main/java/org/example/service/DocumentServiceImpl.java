package org.example.service;

import org.example.interfaces.DocumentRepository;
import org.example.model.Document;
import org.example.interfaces.DocumentService;
import java.util.List;

public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repository;

    public DocumentServiceImpl(DocumentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createDocument(Document document) {
        repository.add(document);
    }

    @Override
    public List<Document> getAllDocuments() {
        return repository.getAll();
    }

    @Override
    public void deleteDocument(Document document) {
        repository.remove(document);
    }

    @Override
    public void clearAll() {
        repository.clear();
    }
}
