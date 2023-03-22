package edu.northeastern.cs5500.starterbot.model;

public class Dish {
    private String name;
    private double price;

    public Dish(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getDishName(){
        return name;
    }

    public double getPrice(){
        return price;
    }
}
