package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Singleton
@Slf4j
public class StartCommand implements SlashCommandHandler, ButtonHandler {

    private final MenuCommand menuCommand;
    private final CartCommand cartCommand;
    private final CongraCommand congraCommand;
    private final CartController cartController; // added a controller to access carts
    private final CongraCommand globalCongraCommand;

    @Inject
    public StartCommand(
            MenuCommand menuCommand,
            CartCommand cartCommand,
            CongraCommand congraCommand,
            CartController cartController) {
        this.menuCommand = menuCommand;
        this.cartCommand = cartCommand;
        this.congraCommand = congraCommand;
        this.cartController = cartController;
        this.globalCongraCommand = new CongraCommand(cartController);
    }

    @Override
    @Nonnull
    public String getName() {
        return "start";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(
                getName(), "Provide a start page to users"); // Changed command description
    }

    /**
     * Rivised the start page implemented by buttons rather than dropdown. Added a picture from
     * online src. There are three buttons in the start page: menu, cart, and cancel.
     */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /start");

        EmbedBuilder embedBuilder =
                new EmbedBuilder()
                        .setTitle("Welcome to the Panda Bot!")
                        .setDescription("Choose what you need.")
                        .setThumbnail(
                                "https://img.freepik.com/premium-vector/panda-food-logo_272290-267.jpg");

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(this.getName() + ":menu", "Menu"),
                        Button.primary(this.getName() + ":view-cart", "View Cart"),
                        Button.danger(this.getName() + ":cancel", "Cancel"));

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        event.reply(messageCreateBuilder.build()).queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String response = event.getButton().getId().split(":")[1];
        Objects.requireNonNull(response);
        String discordUserId = event.getUser().getId();

        event.deferReply().queue(); // Acknowledge the interaction first

        switch (response) {
            case "menu":
                menuCommand.sendMenu(event);
                break;
            case "view-cart":
                cartCommand.sendCart(event);
                break;
            case "cancel":
                // Reset the bot's state to the initial state
                // resetBotState();
                cartController.clearCart(discordUserId);
                // Send a message to the user to confirm that the operation was cancelled
                event.getHook()
                        .sendMessage(" Thank you for visiting FoodiePanda, please come again.")
                        .queue();
                break;
                // case "checkout":
                // int orderNumber = cartCommand.getCart().getNextOrderNumber();
                // globalCongraCommand.sendCongra(event, orderNumber);
                // break;
                // I'm not sure if we will use checkout, just keep it here
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    /** Resets the bot's state to the initial state. */
    // private void resetBotState() {
    //     // Reset any relevant state variables here
    //     cartCommand.getCart().clear();
    // }
}
