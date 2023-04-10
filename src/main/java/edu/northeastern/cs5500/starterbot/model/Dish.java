package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class Dish {
    private String name;
    private double price;
    ObjectId id;

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getDishName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // @Override
    // public boolean equals(Object addedDish) {
    //     if (this == addedDish) {
    //         return true;
    //     }
    //     if (addedDish == null || getClass() != addedDish.getClass()) {
    //         return false;
    //     }
    //     Dish dish = (Dish) addedDish;
    //     return Double.compare(dish.price, price) == 0 && Objects.equals(name, dish.name);
    // }

    // @Override
    // public int hashCode() {
    //     return Objects.hash(name, price);
    // }
}
