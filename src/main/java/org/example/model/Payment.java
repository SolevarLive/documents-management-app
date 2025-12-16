package org.example.model;

import java.time.LocalDate;

public class Payment extends Document {
    private final String employee;

    public Payment(String number, LocalDate date, String user, double amount, String employee) {
        super(number, date, user, amount);
        this.employee = employee;
    }

    @Override
    public String getTypeName() { return "Платёжка"; }

    @Override
    public String toListString() {
        return String.format("Платёжка №%s от %s", getNumber(), formatDate());
    }

    @Override
    public String toDetailedString() {
        return String.format("""
                Платёжка №%s
                Дата: %s
                Пользователь: %s
                Сумма: %.2f
                Сотрудник: %s""",
                getNumber(), formatDate(), getUser(), getAmount(), employee);
    }
}
