package edu.northeastern.cs5500.starterbot.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Data;
import org.bson.types.ObjectId;

@Data // a part of Lombok library to generate standard getters and setters.
@Singleton
public class Cart implements Model {
    private Map<Dish, Integer> items;
    private static AtomicInteger counter = new AtomicInteger(-1);
    private int orderNumber;
    ObjectId id;
    String discordUserId;

    @Inject
    public Cart() {
        items = new HashMap<>();
        this.orderNumber = getNextOrderNumber();
    }

    public void addDish(Dish dish) {
        items.put(dish, items.getOrDefault(dish, 0) + 1);
    }

    public Map<Dish, Integer> getCart() {
        return items;
    }

    public double getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Dish, Integer> entry : items.entrySet()) {
            Dish dish = entry.getKey();
            int count = entry.getValue();
            double price = dish.getPrice();
            totalPrice += price * count;
        }
        return totalPrice;
    }

    public int getNextOrderNumber() {
        orderNumber = counter.incrementAndGet();
        return orderNumber;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void clear() {
        items.clear();
    }

    public void deleteDish(Dish dish) {
        items.remove(dish);
    }
}
