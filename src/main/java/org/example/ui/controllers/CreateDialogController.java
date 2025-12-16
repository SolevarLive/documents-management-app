package org.example.ui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.interfaces.DocumentService;
import org.example.model.Invoice;
import org.example.model.Payment;
import org.example.model.PaymentRequest;

import java.time.LocalDate;

public class CreateDialogController {
    private DocumentService service;

    @FXML private javafx.scene.control.TextField invoiceNumberField, invoiceUserField;
    @FXML private javafx.scene.control.TextField invoiceCurrencyField, invoiceProductField;
    @FXML private javafx.scene.control.TextField invoiceAmountField, invoiceQuantityField, invoiceRateField;
    @FXML private javafx.scene.control.DatePicker invoiceDatePicker;
    @FXML private Button createInvoiceButton;

    @FXML private javafx.scene.control.TextField paymentNumberField, paymentUserField;
    @FXML private javafx.scene.control.TextField paymentAmountField, paymentEmployeeField;
    @FXML private javafx.scene.control.DatePicker paymentDatePicker;
    @FXML private Button createPaymentButton;

    @FXML private javafx.scene.control.TextField requestNumberField, requestUserField;
    @FXML private javafx.scene.control.TextField requestCounterpartyField, requestCurrencyField;
    @FXML private javafx.scene.control.TextField requestAmountField, requestRateField, requestCommissionField;
    @FXML private javafx.scene.control.DatePicker requestDatePicker;
    @FXML private Button createRequestButton;

    public void setService(DocumentService service) {
        this.service = service;
    }

    @FXML private void handleCreateInvoice() {
        try {
            String number = invoiceNumberField.getText();
            LocalDate date = invoiceDatePicker.getValue();
            String user = invoiceUserField.getText();
            double amount = Double.parseDouble(invoiceAmountField.getText());
            String currency = invoiceCurrencyField.getText();
            double rate = Double.parseDouble(invoiceRateField.getText());
            String product = invoiceProductField.getText();
            double quantity = Double.parseDouble(invoiceQuantityField.getText());

            service.createDocument(new Invoice(number, date, user, amount, currency, rate, product, quantity));
            closeDialog();
        } catch (Exception e) {
            showError("Ошибка создания накладной: " + e.getMessage());
        }
    }

    @FXML private void handleCreatePayment() {
        try {
            String number = paymentNumberField.getText();
            LocalDate date = paymentDatePicker.getValue();
            String user = paymentUserField.getText();
            double amount = Double.parseDouble(paymentAmountField.getText());
            String employee = paymentEmployeeField.getText();

            service.createDocument(new Payment(number, date, user, amount, employee));
            closeDialog();
        } catch (Exception e) {
            showError("Ошибка создания платёжки: " + e.getMessage());
        }
    }

    @FXML private void handleCreateRequest() {
        try {
            String number = requestNumberField.getText();
            LocalDate date = requestDatePicker.getValue();
            String user = requestUserField.getText();
            double amount = Double.parseDouble(requestAmountField.getText());
            String counterparty = requestCounterpartyField.getText();
            String currency = requestCurrencyField.getText();
            double rate = Double.parseDouble(requestRateField.getText());
            double commission = Double.parseDouble(requestCommissionField.getText());

            service.createDocument(new PaymentRequest(number, date, user, amount,
                    counterparty, currency, rate, commission));
            closeDialog();
        } catch (Exception e) {
            showError("Ошибка создания заявки: " + e.getMessage());
        }
    }

    @FXML private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) createInvoiceButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка ввода");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
