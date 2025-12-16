package org.example.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;

@JsonTypeName("Invoice")
public class Invoice extends Document {
    private final String currency;
    private final double exchangeRate;
    private final String product;
    private final double quantity;

    public Invoice(String number, LocalDate date, String user, double amount,
                   String currency, double exchangeRate, String product, double quantity) {
        super(number, date, user, amount);
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.product = product;
        this.quantity = quantity;
    }

    public String getCurrency() { return currency; }
    public double getExchangeRate() { return exchangeRate; }
    public String getProduct() { return product; }
    public double getQuantity() { return quantity; }

    @Override
    public String getTypeName() { return "Накладная"; }

    @Override
    public String toListString() {
        return String.format("Накладная №%s от %s", getNumber(), formatDate());
    }

    @Override
    public String toDetailedString() {
        return String.format("""
                Накладная №%s
                Дата: %s
                Пользователь: %s
                Сумма: %.2f %s
                Курс валюты: %.4f
                Товар: %s
                Количество: %.2f""",
                getNumber(), formatDate(), getUser(), getAmount(), currency,
                exchangeRate, product, quantity);
    }
}
