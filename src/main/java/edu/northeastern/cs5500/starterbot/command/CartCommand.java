package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
public class CartCommand implements SlashCommandHandler, ButtonHandler {

    @Inject CartController cartController;
    @Inject CartRender cartRender;
    @Inject MenuRender menuRender;
    @Inject CheckoutRender checkoutRender;
    @Inject DeleteCommand deleteCommand;

    @Inject
    public CartCommand() {
        // left blank for Dagger injection
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
        String discordUserId = event.getUser().getId();
        cartRender.renderCart(event.getHook(), discordUserId);
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String id = Objects.requireNonNull(event.getButton().getId());
        final String response = id.split(":")[1];
        event.deferReply().queue();
        switch (response) {
            case "add-more":
                menuRender.renderMenu(event.getHook());
                break;
            case "delete":
                deleteCommand.sendDelete(event);
                break;
            case "checkout":
                checkoutRender.renderCheckout(event.getHook(), event.getUser().getId());
                break;
            case "cancel":
                clearCart(event);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    /**
     * Menu can be sent when somebody clicked a button.
     *
     * @param event
     */
    public void sendCart(@Nonnull ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        cartRender.renderCart(event.getHook(), discordUserId);
    }

    public void clearCart(ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        cartController.clearCart(discordUserId);
        event.getHook()
                .sendMessage("Your cart has been cleared. Thanks for visiting FoodiePanda.")
                .queue();
    }
}
