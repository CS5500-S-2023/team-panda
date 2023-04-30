package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.controller.MenuController;
import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
public class MenuCommand implements SlashCommandHandler, StringSelectHandler {

    @Inject MenuController menuController;
    @Inject CartRender cartRender;
    @Inject MenuRender menuRender;
    @Inject CartController cartController;

    @Inject
    public MenuCommand() {
        // left blank for Dagger injection
    }

    /** The name of this class. */
    @Override
    @Nonnull
    public String getName() {
        return "menu";
    }

    /** The description of this command. */
    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Show menu for users to select dishes.");
    }

    /** Send menu to customers. */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        menuRender.renderMenu(event.getHook());
    }

    /** Send menu to customers. */
    public void sendMenu(@Nonnull StringSelectInteractionEvent event) {
        menuRender.renderMenu(event.getHook());
    }

    /**
     * Menu can be sent when somebody clicked a button.
     *
     * @param event
     */
    public void sendMenu(@Nonnull ButtonInteractionEvent event) {
        menuRender.renderMenu(event.getHook());
    }

    /**
     * After customer chose dish, we can send a message to him, which can let him make sure he has
     * added this dish. At the same time, this dish should be added to his cart. Then, show him his
     * cart.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        final String response = event.getInteraction().getValues().get(0);
        Objects.requireNonNull(response);
        String reply = "You added " + response + " to your cart.";
        double dishPrice = 0.0;
        String discordUserId = event.getUser().getId();

        Set<MenuItem> menuItems = menuController.getMenuItems();
        dishPrice =
                menuItems.stream()
                        .filter(item -> item.getItemName().equals(response))
                        .findFirst()
                        .get()
                        .getPrice();

        if (dishPrice != 0.0) {
            Dish dish = Dish.builder().dishName(response).price(dishPrice).build();
            cartController.addToCart(dish, discordUserId);
            event.reply(reply).queue();
        } else {
            event.reply("Invalid option selected.").queue();
        }

        cartRender.renderCart(event.getHook(), discordUserId);
    }
}
