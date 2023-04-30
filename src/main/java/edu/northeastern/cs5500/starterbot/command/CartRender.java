package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Singleton
@Slf4j
public class CartRender {
    @Inject CartController cartController;

    @Inject
    public CartRender() {
        // left blank for Dagger injection
    }

    /**
     * If the cart is empty, display the total price as 0 and provide the user options to menu and
     * cancel. If there is something in the cart, list them and display the total price and provide
     * the user options to add more, delete, checkout, and cancel.
     *
     * @param hook
     */
    public void renderCart(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /cart");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Cart");
        embedBuilder.setColor(0x1fab89);

        double totalPrice = 0;
        if (!cartController.getDishesForUser(discordUserId).isEmpty()) {
            for (Map.Entry<Dish, Integer> entry :
                    cartController.getDishesForUser(discordUserId).entrySet()) {
                Dish dish = entry.getKey();
                int quantity = entry.getValue();
                String itemName = String.format("%s (x%d)", dish.getDishName(), quantity);
                String itemPrice = String.format("$%.2f", dish.getPrice());
                if (itemName == null || itemPrice == null) {
                    // can never happen because the first arguments to String.format are not null
                    throw new NullPointerException();
                }
                embedBuilder.addField(itemName, itemPrice, false);
                totalPrice += dish.getPrice() * quantity;
            }
        } else {
            embedBuilder.setDescription("Your cart is empty.");
        }
        embedBuilder.setFooter(String.format("Total Price: $%.2f", totalPrice));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        if (!cartController.getDishesForUser(discordUserId).isEmpty()) {
            messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                            Button.primary("cart:add-more", "Add More"),
                            Button.primary("cart:delete", "Delete"),
                            Button.success("cart:checkout", "Checkout"),
                            Button.danger("cart:cancel", "Cancel"));
        } else {
            messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                            Button.primary("cart:add-more", "Add more"),
                            Button.danger("cart:cancel", "Cancel"));
        }

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }
}
