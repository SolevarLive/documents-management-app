package org.example.model;

import com.fasterxml.jackson.annotation.JsonTypeName;

import java.time.LocalDate;

@JsonTypeName("Payment")
public class Payment extends Document {
    private final String employee;

    public Payment(String number, LocalDate date, String user, double amount, String employee) {
        super(number, date, user, amount);
        this.employee = employee;
    }

    public String getEmployee() { return employee; }

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
