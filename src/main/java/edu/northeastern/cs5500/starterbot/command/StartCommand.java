package edu.northeastern.cs5500.starterbot.command;

import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

@Singleton
@Slf4j
public class StartCommand implements SlashCommandHandler, StringSelectHandler {

    private final MenuCommand menuCommand;
    private final CartCommand cartCommand;

    @Inject
    public StartCommand(MenuCommand menuCommand, CartCommand cartCommand) {
        this.menuCommand = menuCommand;
        this.cartCommand = cartCommand;
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

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /start");

        StringSelectMenu menu =
                StringSelectMenu.create("start")
                        .setPlaceholder(
                                "Choose what you need.") // shows the placeholder indicating what
                        // this menu is for
                        .addOption("Menu", "menu")
                        .addOption("View Cart", "view-cart")
                        .addOption("View Queue", "view-queue")
                        .addOption("Checkout", "checkout")
                        .build();
        event.reply("Let's start your order!").setEphemeral(true).addActionRow(menu).queue();
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0);
        Objects.requireNonNull(response);

        switch (response) {
            case "menu":
                menuCommand.showMenu(event);
                break;
            case "view-cart":
                cartCommand.showCart(event);
                break;
            case "view-queue":
                // Handle view queue action here
                break;
            case "checkout":
                // Handle checkout action here
                break;
            default:
                event.reply("Invalid option selected.").queue();
        }
    }
}
