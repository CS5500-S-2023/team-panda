package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDateTime;

public class Order {
    private int orderNumber;
    private LocalDateTime orderTime;
    private Status status;

    public Order(int orderNumber, LocalDateTime orderTime, Status status) {
        this.orderNumber = orderNumber;
        this.orderTime = orderTime;
        this.status = status;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
