package org.example.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.interfaces.DocumentRepository;
import org.example.model.Document;
import org.example.model.Invoice;
import org.example.model.Payment;
import org.example.model.PaymentRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonDocumentRepository implements DocumentRepository {
    private final File file;
    private final ObjectMapper mapper;
    private final List<Document> documents = new ArrayList<>();

    public JsonDocumentRepository(File file) {
        this.file = file;
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Override
    public void add(Document document) {
        documents.add(document);
        save();
    }

    @Override
    public List<Document> getAll() {
        load();
        return new ArrayList<>(documents);
    }

    @Override
    public void remove(Document document) {
        documents.remove(document);
        save();
    }

    @Override
    public void clear() {
        documents.clear();
        save();
    }

    private void save() {
        try {
            List<Map<String, Object>> serializableDocs = documents.stream()
                    .map(doc -> {
                        Map<String, Object> map = new java.util.LinkedHashMap<>();
                        map.put("@type", doc.getClass().getSimpleName());
                        map.put("number", doc.getNumber());
                        map.put("date", doc.getDate());
                        map.put("user", doc.getUser());
                        map.put("amount", doc.getAmount());

                        if (doc instanceof Invoice inv) {
                            map.put("currency", inv.getCurrency());
                            map.put("exchangeRate", inv.getExchangeRate());
                            map.put("product", inv.getProduct());
                            map.put("quantity", inv.getQuantity());
                        } else if (doc instanceof Payment pay) {
                            map.put("employee", pay.getEmployee());
                        } else if (doc instanceof PaymentRequest req) {
                            map.put("counterparty", req.getCounterparty());
                            map.put("currency", req.getCurrency());
                            map.put("exchangeRate", req.getExchangeRate());
                            map.put("commission", req.getCommission());
                        }

                        return map;
                    })
                    .collect(java.util.stream.Collectors.toList());

            mapper.writeValue(file, serializableDocs);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения: " + e.getMessage());
        }
    }


    private void load() {
        if (file.exists()) {
            try {
                List<?> rawList = mapper.readValue(file, List.class);
                documents.clear();

                for (Object item : rawList) {
                    if (item instanceof java.util.Map) {
                        @SuppressWarnings("unchecked")
                        var map = (java.util.Map<String, Object>) item;

                        String type = (String) map.get("@type");
                        String number = (String) map.get("number");
                        LocalDate date = mapper.convertValue(map.get("date"), LocalDate.class);
                        String user = (String) map.get("user");
                        Double amountObj = (Number) map.get("amount") != null ? ((Number) map.get("amount")).doubleValue() : 0.0;
                        double amount = amountObj;

                        Document doc = switch (type) {
                            case "Invoice" -> {
                                String currency = map.get("currency") != null ? (String) map.get("currency") : "";
                                Double rateObj = map.get("exchangeRate") != null ? ((Number) map.get("exchangeRate")).doubleValue() : 1.0;
                                String product = map.get("product") != null ? (String) map.get("product") : "";
                                Double qtyObj = map.get("quantity") != null ? ((Number) map.get("quantity")).doubleValue() : 0.0;
                                yield new Invoice(number, date, user, amount, currency, rateObj, product, qtyObj);
                            }
                            case "Payment" -> {
                                String employee = map.get("employee") != null ? (String) map.get("employee") : "";
                                yield new Payment(number, date, user, amount, employee);
                            }
                            case "PaymentRequest" -> {
                                String counterparty = map.get("counterparty") != null ? (String) map.get("counterparty") : "";
                                String currency = map.get("currency") != null ? (String) map.get("currency") : "";
                                Double rateObj = map.get("exchangeRate") != null ? ((Number) map.get("exchangeRate")).doubleValue() : 1.0;
                                Double commObj = map.get("commission") != null ? ((Number) map.get("commission")).doubleValue() : 0.0;
                                yield new PaymentRequest(number, date, user, amount, counterparty, currency, rateObj, commObj);
                            }
                            default -> null;
                        };

                        if (doc != null) {
                            documents.add(doc);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Ошибка загрузки: " + e.getMessage());
            }
        }
    }
}
