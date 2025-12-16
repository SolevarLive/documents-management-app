package org.example.interfaces;

import org.example.model.Document;
import java.util.List;

public interface DocumentService {
    void createDocument(Document document);
    List<Document> getAllDocuments();
    void deleteDocument(Document document);
    void clearAll();
}
