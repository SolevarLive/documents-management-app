package org.example.repository;

import org.example.model.Document;
import org.example.interfaces.DocumentRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDocumentRepository implements DocumentRepository {
    private final List<Document> documents = new ArrayList<>();

    @Override
    public void add(Document document) {
        documents.add(document);
    }

    @Override
    public List<Document> getAll() {
        return new ArrayList<>(documents);
    }

    @Override
    public void remove(Document document) {
        documents.removeIf(d -> d == document);
    }

    @Override
    public void clear() {
        documents.clear();
    }
}
