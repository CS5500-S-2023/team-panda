package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Dish;
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
    Cart cart = new Cart();

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
                        .addOption("Chow Mein ($1.5)", "chow-mein")
                        .addOption("Orange Chicken ($3.5)", "orange-chicken")
                        .addOption("Honey Walnut Shrimp ($3)", "honey-walnut-shrimp")
                        .addOption("Mushroom Chicken ($3)", "mushroom-chicken")
                        .addOption("Broccoli Beef ($3)", "broccoli-beef")
                        .build();
        event.reply("Please pick your dishes").setEphemeral(true).addActionRow(menu).queue();
    }

    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0);
        Objects.requireNonNull(response);
        String reply = "You added " + response + " to your cart.";
        double dishPrice = 0.0;
        if (!response.equals("")) {
            switch (response) {
                case "chow-mein":
                    dishPrice = 1.5;
                    break;
                case "orange-chicken":
                    dishPrice = 3.5;
                    break;
                case "honey-walnut-shrimp":
                    dishPrice = 3.0;
                    break;
                case "mushroom-chicken":
                    dishPrice = 3.0;
                    break;
                case "broccoli-beef":
                    dishPrice = 3.0;
                    break;
                default:
                    event.reply("Invalid dish name.").queue();
            }
        }
        if (dishPrice != 0.0) {
            Dish dish = new Dish(response, dishPrice);
            cart.addDish(dish);
            event.reply(reply).queue();
        }
    }
}
