package org.example.ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import org.example.interfaces.DocumentService;
import org.example.model.Document;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private DocumentService service;

    @FXML private TableView<Document> documentsTable;
    @FXML private TableColumn<Document, Boolean> selectColumn;
    @FXML private TableColumn<Document, String> typeColumn;
    @FXML private TableColumn<Document, String> numberColumn;
    @FXML private TableColumn<Document, String> dateColumn;

    @FXML private Button createButton;
    @FXML private Button saveButton;
    @FXML private Button loadButton;
    @FXML private Button viewButton;
    @FXML private Button deleteButton;

    public MainController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectColumn.setCellFactory(p -> new CheckBoxTableCell<>());
        typeColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTypeName()));
        numberColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getNumber()));
        dateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().formatDate()));

        refreshTable();
        deleteButton.disableProperty().bind(
                documentsTable.getSelectionModel().selectedItemProperty().isNull());
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    @FXML private void handleCreate() { /* заглушка */ }
    @FXML private void handleSave() { /* заглушка */ }
    @FXML private void handleLoad() { /* заглушка */ }
    @FXML private void handleView() {
        Document selected = documentsTable.getSelectionModel().getSelectedItem();
        if (selected != null && service != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Просмотр документа");
            alert.setHeaderText(selected.getTypeName());
            alert.setContentText(selected.toDetailedString());
            alert.show();
        }
    }
    @FXML private void handleDelete() {
        if (service != null) {
            var selectedItems = documentsTable.getSelectionModel().getSelectedItems();
            if (!selectedItems.isEmpty()) {
                for (Document doc : selectedItems) {
                    service.deleteDocument(doc);
                }
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        if (service != null) {
            List<Document> documents = service.getAllDocuments();
            documentsTable.getItems().setAll(documents);
        }
    }
}
