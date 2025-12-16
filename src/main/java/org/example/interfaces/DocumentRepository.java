package org.example.interfaces;

import org.example.model.Document;
import java.util.List;

public interface DocumentRepository {
    void add(Document document);
    List<Document> getAll();
    void remove(Document document);
    void clear();
}
