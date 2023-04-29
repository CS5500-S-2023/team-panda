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
        return "addmenuitem";
    }

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

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /addMenuItem");
        String itemName = Objects.requireNonNull(event.getOption("itemname")).getAsString();

        String discordUserId = event.getUser().getId();
        System.out.println("Start>>>>>>>>>" + discordUserId + "<<<<<End here");

        String itemPrice = Objects.requireNonNull(event.getOption("itemprice")).getAsString();

        boolean operation = false;
        if (!menuController.isMenuOwner(discordUserId)) {
            event.reply("You are not the owner of this store.").queue();
            return;
        } else {
            operation = menuController.addMenuItem(itemName, itemPrice);
        }
        if (operation) {
            event.reply(itemName + " added successfully.").queue();
        } else {
            event.reply(itemName + " could not be added.").queue();
        }
    }
}
