package edu.northeastern.cs5500.starterbot.model;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Data;
import org.bson.types.ObjectId;

@Data // a part of Lombok library to generate standard getters and setters.
@Singleton
public class Cart implements Model {
    private Map<Dish, Integer> items;
    ObjectId id;
    String discordUserId;

    @Inject
    public Cart() {
        items = new HashMap<>();
    }

    public void addDish(Dish dish) {
        items.put(dish, items.getOrDefault(dish, 0) + 1);
    }

    /**
     * Gets the total price of dishes in this cart.
     *
     * @return The toal price
     */
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

    public void clear() {
        items.clear();
    }

    /**
     * Delete a certain dish from this cart. It will return true if it was successfully deleted,
     * return false otherwise.
     *
     * @param dish The dish that the customer wants to delete
     * @return Return true if deleted successfully, false otherwise
     */
    public boolean deleteDish(Dish dish) {
        if (items.containsKey(dish)) {
            int count = items.get(dish);
            if (count == 1) {
                items.remove(dish);
            } else {
                items.put(dish, count - 1);
            }
            return true;
        }
        return false;
    }
}
