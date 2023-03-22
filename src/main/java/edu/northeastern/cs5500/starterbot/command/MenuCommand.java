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
public class MenuCommand implements SlashCommandHandler, StringSelectHandler {

    @Inject
    public MenuCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "menu";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Show menu for users to select dishes.");
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /menu");

        StringSelectMenu menu =
                StringSelectMenu.create("menu")
                        .setPlaceholder("Please select your dishes.")
                        .addOption(
                                "Chow Mein",
                                "chow-mein",
                                "$1.5") // modified price presentation for all dishes
                        .addOption("Orange Chicken", "orange-chicken", "$3.5")
                        .addOption("Honey Walnut Shrimp", "honey-walnut-shrimp", "$3")
                        .addOption("Mushroom Chicken", "mushroom-chicken", "$3")
                        .addOption("Broccoli Beef", "broccoli-beef", "$3")
                        .build();
        event.reply("Please pick your dishes").setEphemeral(true).addActionRow(menu).queue();
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0);
        Objects.requireNonNull(response);
        event.reply("You added: " + response).queue();
        // if (response == "chow-mein") {
        //     String dishName = event.getOption("chow-mein").getAsString();
        // } remember to try evet.getOption().getDescriton()
    }
}
