package edu.northeastern.cs5500.starterbot.command;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Singleton
@Slf4j
public class StartCommand implements SlashCommandHandler, StringSelectHandler {

    private final MenuCommand menuCommand;
    private final CartCommand cartCommand;
    private final CongraCommand congraCommand;
    private final CongraCommand globalCongraCommand;

    @Inject
    public StartCommand(
            MenuCommand menuCommand, CartCommand cartCommand, CongraCommand congraCommand) {
        this.menuCommand = menuCommand;
        this.cartCommand = cartCommand;
        this.congraCommand = congraCommand;
        this.globalCongraCommand = new CongraCommand(cartCommand.getCart());
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
     * Rivised the start page implemented by buttons rather than dropdown. Added a picture
     * from online src. There are three buttons in the start page: menu, cart, and cancel.
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
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0);
        Objects.requireNonNull(response);

        event.deferReply().queue(); // Acknowledge the interaction first

        switch (response) {
            case "menu":
                menuCommand.sendMenu(event);
                break;
            case "view-cart":
                cartCommand.sendCart(event);
                break;
            case "view-queue":
                // Handle view queue action here
                break;
            case "checkout":
                int orderNumber = cartCommand.getCart().getNextOrderNumber();
                globalCongraCommand.sendCongra(event, orderNumber);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }
}
