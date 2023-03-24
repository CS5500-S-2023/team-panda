package edu.northeastern.cs5500.starterbot.model;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Cart {
    private Map<Dish, Integer> items;

    @Inject
    public Cart() {
        items = new HashMap<>();
    }

    public void addDish(Dish dish) {
        items.put(dish, items.getOrDefault(dish, 0) + 1);
    }

    public Map<Dish, Integer> getCart() {
        return items;
    }

    public void clear() {
        items.clear();
    }

    public void deleteDish(String name) {
        items.entrySet().removeIf(entry -> entry.getKey().getDishName().equals(name));
    }
}
