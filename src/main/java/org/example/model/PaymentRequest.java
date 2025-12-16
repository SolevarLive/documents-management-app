package org.example.model;

import java.time.LocalDate;

public class PaymentRequest extends Document {
    private final String counterparty;
    private final String currency;
    private final double exchangeRate;
    private final double commission;

    public PaymentRequest(String number, LocalDate date, String user, double amount,
                          String counterparty, String currency, double exchangeRate, double commission) {
        super(number, date, user, amount);
        this.counterparty = counterparty;
        this.currency = currency;
        this.exchangeRate = exchangeRate;
        this.commission = commission;
    }

    @Override
    public String getTypeName() { return "Заявка на оплату"; }

    @Override
    public String toListString() {
        return String.format("Заявка №%s от %s", getNumber(), formatDate());
    }

    @Override
    public String toDetailedString() {
        return String.format("""
                Заявка на оплату №%s
                Дата: %s
                Пользователь: %s
                Контрагент: %s
                Сумма: %.2f %s
                Курс валюты: %.4f
                Комиссия: %.2f""",
                getNumber(), formatDate(), getUser(), counterparty, getAmount(),
                currency, exchangeRate, commission);
    }
}
