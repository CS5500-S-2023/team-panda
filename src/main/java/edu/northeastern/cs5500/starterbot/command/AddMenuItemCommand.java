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
    public AddMenuItemCommand() {
        // left blank for Dagger injection
    }

    /** The name of this class. */
    @Override
    @Nonnull
    public String getName() {
        return "addmenuitem";
    }

    /**
     * If user want to add menuItem to the database, they can type in the name and price of this
     * item. These two can not be null.
     */
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Add a new item to the menu")
                .addOptions(
                        new OptionData(OptionType.STRING, "itemname", "Name of the item")
                                .setRequired(true),
                        new OptionData(OptionType.STRING, "itemprice", "Price of the item")
                                .setRequired(true));
    }

    /**
     * After getting the name and the price of item that the user want to add, do the owner check
     * first. If this user is not the owner of this menu, a message "You are not the owner of this
     * store." will be sent. If this item has been added, a message "added to menu successfully."
     * will be sent. If this items already exist, or added failed, a message " could not be added."
     * will be sent.
     */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /addMenuItem");
        String itemName = Objects.requireNonNull(event.getOption("itemname")).getAsString();

        String discordUserId = event.getUser().getId();

        String itemPrice = Objects.requireNonNull(event.getOption("itemprice")).getAsString();

        boolean operation = false;
        if (!menuController.isMenuOwner(discordUserId)) {
            event.reply("You are not the owner of this store.").queue();
            return;
        } else {
            operation = menuController.addMenuItem(itemName, itemPrice);
        }
        if (operation) {
            event.reply(itemName + " added to menu successfully.").queue();
        } else {
            event.reply(itemName + " could not be added.").queue();
        }
    }
}
