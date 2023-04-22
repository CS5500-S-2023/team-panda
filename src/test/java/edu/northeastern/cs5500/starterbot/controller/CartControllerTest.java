package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;

class CartControllerTest {
    static final String USER_ID_1 = "28484913146489";

    private CartController getCartController() {
        return new CartController(new InMemoryRepository<>());
    }

    Dish createTestDish() {
        return Dish.builder().dishName("rice").price(0.99).build();
    }

    @Test
    void testGetItemsInCart() {
        CartController cartController = getCartController();
        // test empty cart

        Dish dish = createTestDish();
        assertThat(cartController.getItemsInCart(USER_ID_1)).doesNotContainKey(dish);

        // test non-empty cart
        cartController.addToCart(dish, USER_ID_1);
        cartController.addToCart(dish, USER_ID_1);
        assertThat(cartController.getItemsInCart(USER_ID_1)).containsKey(dish);
    }

    @Test
    void testAddToCart() {
        // emtpy cart
        CartController cartController = getCartController();
        Dish dish = createTestDish();
        assertThat(cartController.getItemsInCart(USER_ID_1)).isEmpty();
        // add to cart
        cartController.addToCart(dish, USER_ID_1);
        // non-empty cart
        assertThat(cartController.getItemsInCart(USER_ID_1)).containsKey(dish);
    }

    @Test
    void testRemoveFromCart() {
        // add to cart
        CartController cartController = getCartController();
        Dish dish = createTestDish();
        cartController.addToCart(dish, USER_ID_1);
        assertThat(cartController.getItemsInCart(USER_ID_1)).containsKey(dish);
        // remove from cart
        cartController.removeFromCart(dish, USER_ID_1);
        // cart after removal
        assertThat(cartController.getItemsInCart(USER_ID_1)).doesNotContainKey(dish);
    }

    @Test
    void testGetCartForUser() {
        // test get empty cart for user
        CartController cartController = getCartController();
        Cart cart = cartController.getCartForUser(USER_ID_1);
        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getDiscordUserId()).isEqualTo(USER_ID_1);

        // test get non-empty cart for user
        Dish dish = createTestDish();
        cartController.addToCart(dish, USER_ID_1);
        assertThat(cart.getItems()).isNotEmpty();
    }

    @Test
    void testClearCart() {
        // add to cart
        CartController cartController = getCartController();
        Dish dish = createTestDish();
        cartController.addToCart(dish, USER_ID_1);
        assertThat(cartController.getItemsInCart(USER_ID_1)).containsKey(dish);
        // clear cart
        cartController.clearCart(USER_ID_1);
        // empty cart
        assertThat(cartController.getItemsInCart(USER_ID_1)).isEmpty();
    }
}
