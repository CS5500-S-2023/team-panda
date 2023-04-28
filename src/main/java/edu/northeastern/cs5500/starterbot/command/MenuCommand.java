package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.controller.MenuController;
import edu.northeastern.cs5500.starterbot.model.Dish;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class MenuCommand implements SlashCommandHandler, StringSelectHandler {

    private final CartController cartController;
    private final Provider<CartCommand> cartCommandProvider;
    @Inject MenuController menuController;

    @Inject
    public MenuCommand(CartController cartController, Provider<CartCommand> cartCommandProvider) {
        this.cartController = cartController;
        this.cartCommandProvider = cartCommandProvider;
    }

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

    private void displayMenu(@Nonnull InteractionHook hook) {
        log.info("event: /menu");

        Set<MenuItem> menuItems = menuController.getMenuItems();

        if (menuItems == null) {
            hook.sendMessage("No dish available.").queue();
            return;
        }

        StringSelectMenu.Builder menuBuilder =
                StringSelectMenu.create("menu").setPlaceholder("Click to add a dish to your cart.");
        for (MenuItem menuItem : menuItems) {
            menuBuilder.addOption(
                    menuItem.getItemName(),
                    menuItem.getItemName(),
                    "$" + String.valueOf(menuItem.getPrice()));
        }

        StringSelectMenu menu = menuBuilder.build();

        MessageCreateData messageCreateData = new MessageCreateBuilder().addActionRow(menu).build();
        hook.sendMessage(messageCreateData).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        displayMenu(event.getHook());
    }

    public void sendMenu(@Nonnull StringSelectInteractionEvent event) {
        displayMenu(event.getHook());
    }

    /**
     * Menu can be sent when somebody clicked a button.
     *
     * @param event
     */
    public void sendMenu(@Nonnull ButtonInteractionEvent event) {
        displayMenu(event.getHook());
    }

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

        // TODO: remove the cartCommandProvide once we solove the cyclic depency issue.
        cartCommandProvider.get().displayCart(event.getHook(), discordUserId);
    }
}
