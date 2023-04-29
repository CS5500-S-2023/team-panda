package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.MenuController;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Singleton
@Slf4j
public class AddMenuItemCommand implements SlashCommandHandler {

    @Inject MenuController menuController;

    @Inject
    public AddMenuItemCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "addMenuItem";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Add a new item to the menu")
                .addOptions(
                        new OptionData(OptionType.STRING, "itemName", "Name of the item")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "itemPrice", "Price of the item")
                                .setRequired(true));
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /addMenuItem");
        String itemName = Objects.requireNonNull(event.getOption("itemName")).getAsString();

        String discordUserId = event.getUser().getId();

        String itemPrice = Objects.requireNonNull(event.getOption("itemPrice")).getAsString();

        boolean operation = false;
        if (!menuController.isMenuOwner(discordUserId)) {
            event.reply("You are not the owner of this store").queue();
            return;
        } else {
            operation = menuController.addMenuItem(itemName, itemPrice);
        }
        if (operation) {
            event.reply("Item added successfully").queue();
        } else {
            event.reply("Item could not be added").queue();
        }
    }
}
