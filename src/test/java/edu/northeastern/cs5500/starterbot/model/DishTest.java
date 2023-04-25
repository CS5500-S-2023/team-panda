/**
 * This contains JUnit tests for the Dish class. Since the Dish class is using @Data noatation, we
 * do not test getters, setters and equality.
 */
package edu.northeastern.cs5500.starterbot.model;

import static com.google.common.truth.Truth.assertThat;

import org.junit.jupiter.api.Test;

class DishTest {
    private static final String DISH_NAME = "Rice";
    private static final double PRICE = 1.5;

    @Test
    // Test for the constructor.
    void testDishConstrucotr() {
        Dish dish = Dish.builder().dishName(DISH_NAME).price(PRICE).build();
        // Dish dish = new Dish(DISH_NAME, PRICE);
        assertThat(dish.getDishName()).isEqualTo(DISH_NAME);
        assertThat(dish.getPrice()).isEqualTo(PRICE);
    }
}
