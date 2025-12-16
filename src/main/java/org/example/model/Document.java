package org.example.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Invoice.class, name = "Invoice"),
        @JsonSubTypes.Type(value = Payment.class, name = "Payment"),
        @JsonSubTypes.Type(value = PaymentRequest.class, name = "PaymentRequest")
})
public abstract class Document implements Serializable {
    private final String number;
    private final LocalDate date;
    private final String user;
    private final double amount;

    protected Document(String number, LocalDate date, String user, double amount){
        this.amount=amount;
        this.date=date;
        this.user=user;
        this.number=number;
    }

    public String getNumber(){return number;}
    public LocalDate getDate() {return date;}
    public String getUser() {return user;}
    public  double getAmount() {return amount;}

    public abstract String getTypeName();
    public abstract String toListString();
    public abstract String toDetailedString();

    public String formatDate() {
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Override
    public String toString(){
        return toListString();
    }
}
