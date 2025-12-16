package org.example.ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.stage.FileChooser;
import org.example.interfaces.DocumentService;
import org.example.model.Document;
import org.example.repository.JsonDocumentRepository;

import java.io.File;
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

    @FXML private void handleCreate() {
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(
                "Накладная", javafx.collections.FXCollections.observableArrayList("Накладная", "Платёжка", "Заявка на оплату"));
        dialog.setTitle("Создать документ");
        dialog.setHeaderText("Выберите тип документа");

        dialog.showAndWait().ifPresent(type -> {
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/create-dialog.fxml"));
                javafx.scene.Scene dialogScene = new javafx.scene.Scene(loader.load(), 500, 600);
                CreateDialogController controller = loader.getController();
                controller.setService(service);

                javafx.stage.Stage dialogStage = new javafx.stage.Stage();
                dialogStage.setScene(dialogScene);
                dialogStage.setTitle("Создать " + type);
                dialogStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
                dialogStage.showAndWait();

                refreshTable();
            } catch (Exception e) {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setContentText("Ошибка открытия диалога: " + e.getMessage());
                alert.show();
            }
        });
    }

    @FXML private void handleSave() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить документы");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showSaveDialog(documentsTable.getScene().getWindow());

        if (file != null) {
            var jsonRepo = new JsonDocumentRepository(file);
            service.clearAll();
            for (Document doc : documentsTable.getItems()) {
                jsonRepo.add(doc);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Сохранено в " + file.getName());
            alert.show();
        }
    }

    @FXML private void handleLoad() {
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить документы");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON", "*.json"));
        File file = fileChooser.showOpenDialog(documentsTable.getScene().getWindow());

        if (file != null) {
            try {
                var jsonRepo = new JsonDocumentRepository(file);
                List<Document> loadedDocuments = jsonRepo.getAll();

                service.clearAll();
                for (Document doc : loadedDocuments) {
                    service.createDocument(doc);
                }
                refreshTable();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Загружено " + loadedDocuments.size() + " документов");
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ошибка загрузки: " + e.getMessage());
                alert.show();
            }
        }
    }


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
