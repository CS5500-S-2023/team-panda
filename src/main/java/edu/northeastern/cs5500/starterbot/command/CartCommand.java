package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class CartCommand implements SlashCommandHandler {
    private final Cart cart;

    @Inject
    public CartCommand(Cart cart) {
        this.cart = cart;
    }

    @Override
    @Nonnull
    public String getName() {
        return "cart";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Display all dishes added to the cart.");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /cart");

        if (cart.getCart().isEmpty()) {
            event.reply("Your cart is empty.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Cart");
        embedBuilder.setColor(0x1fab89); // You can change the color if you like

        double totalPrice = 0;
        for (Map.Entry<Dish, Integer> entry : cart.getCart().entrySet()) {
            Dish dish = entry.getKey();
            int quantity = entry.getValue();
            String itemName = String.format("%s (x%d)", dish.getDishName(), quantity);
            embedBuilder.addField(itemName, String.format("$%.2f", dish.getPrice()), true);
            totalPrice += dish.getPrice() * quantity;
        }

        embedBuilder.setFooter(String.format("Total Price: $%.2f", totalPrice));
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    public void showCart(@Nonnull StringSelectInteractionEvent event) {
        log.info("event: /cart");

        if (cart.getCart().isEmpty()) {
            event.reply("Your cart is empty.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Cart");
        embedBuilder.setColor(0x1fab89); // You can change the color if you like

        double totalPrice = 0;
        for (Map.Entry<Dish, Integer> entry : cart.getCart().entrySet()) {
            Dish dish = entry.getKey();
            int quantity = entry.getValue();
            String itemName = String.format("%s (x%d)", dish.getDishName(), quantity);
            embedBuilder.addField(itemName, String.format("$%.2f", dish.getPrice()), true);
            totalPrice += dish.getPrice() * quantity;
        }

        embedBuilder.setFooter(String.format("Total Price: $%.2f", totalPrice));

        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }
}
