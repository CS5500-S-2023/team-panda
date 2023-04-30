package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.model.Cart;
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
public class CheckoutRender {
    @Inject CartController cartController;

    @Inject
    public CheckoutRender() {
        // left blank for Dagger injection
    }

    public void renderCheckout(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /checkout");

        Cart cart = cartController.getCartForUser(discordUserId);
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("Total Price: " + String.format("$%.2f", cart.getTotalPrice()));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary("checkout:make-payment", "Make Payment"),
                        Button.primary("checkout:cart", "Back to Cart"),
                        Button.danger("checkout:cancel", "Cancel"));

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }
}
