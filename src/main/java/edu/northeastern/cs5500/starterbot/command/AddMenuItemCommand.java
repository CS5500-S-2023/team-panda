package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.MenuController;
import java.util.Objects;
import javax.inject.Inject;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public class AddMenuItemCommand implements SlashCommandHandler {

    @Inject MenuController menuController;

    @Inject
    public AddMenuItemCommand() {}

    @NotNull
    @Override
    public String getName() {
        return "addMenuItem";
    }

    @NotNull
    @Override
    public CommandData getCommandData() {
        return null;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        String itemName = Objects.requireNonNull(event.getOption("itemName")).getAsString();
        String itemPrice = Objects.requireNonNull(event.getOption("itemPrice")).getAsString();
        String discordUserId = event.getUser().getId();

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
