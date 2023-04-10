package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.model.Cart;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Singleton
@Slf4j
public class CheckoutCommand implements SlashCommandHandler, ButtonHandler {

    private final CartCommand cartCommand;
    private final CongraCommand congraCommand;
    private final CongraCommand globalCongraCommand;
    private final CartController cartController;

    @Inject
    public CheckoutCommand(
            CartCommand cartCommand, CartController cartController, CongraCommand congraCommand) {
        this.cartCommand = cartCommand;
        this.congraCommand = congraCommand;
        this.cartController = cartController;
        this.globalCongraCommand = new CongraCommand(cartController);
    }

    @Override
    @Nonnull
    public String getName() {
        return "checkout";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Please checkout here."); // Changed command description
    }

    private void display(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /checkout");

        Cart cart = cartController.getCartForUser(discordUserId);
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("Total Price: " + String.format("$%.2f", cart.getTotalPrice()));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(this.getName() + ":make-payment", "Make Payment"),
                        Button.primary(this.getName() + ":cart", "Back to Cart"),
                        Button.danger(this.getName() + ":cancel", "Cancel"));

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        // display(event.getHook());
        // If you are using hook, please use the upper line and delete the following.
        log.info("event: /checkout");

        String discordUserId = event.getUser().getId();
        Cart cart = cartController.getCartForUser(discordUserId);
        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("Total Price: " + String.format("$%.2f", cart.getTotalPrice()));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(this.getName() + ":make-payment", "Make Payment"),
                        Button.primary(this.getName() + ":cart", "Back to Cart"),
                        Button.danger(this.getName() + ":cancel", "Cancel"));

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        event.reply(messageCreateBuilder.build()).queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String response = event.getButton().getId().split(":")[1];
        Objects.requireNonNull(response);
        String discordUserId = event.getUser().getId();
        Cart cart = cartController.getCartForUser(discordUserId);

        event.deferReply().queue(); // Acknowledge the interaction first

        switch (response) {
            case "make-payment":
                int orderNumber = cart.getNextOrderNumber();
                globalCongraCommand.sendCongra(event, orderNumber);
                break;
            case "cart":
                cartCommand.sendCart(event);
                break;
            case "cancel":
                cartCommand.clearCart(event);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    public void sendCheckout(@Nonnull ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        display(event.getHook(), discordUserId);
    }
}
