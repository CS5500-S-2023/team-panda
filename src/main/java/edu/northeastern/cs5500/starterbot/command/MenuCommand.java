package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.controller.MenuController;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.ArrayList;
import java.util.List;
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

@Singleton
@Slf4j
public class MenuCommand implements SlashCommandHandler, StringSelectHandler {

    // Substituted a cart with a cartController
    private final CartController cartController;
    private final Provider<CartCommand> cartCommandProvider;

    @Inject MenuController menuController;
    // private final CheckoutCommand checkoutCommand;

    @Inject
    public MenuCommand(CartController cartController, Provider<CartCommand> cartCommandProvider) {
        this.cartController = cartController;
        this.cartCommandProvider = cartCommandProvider;
        // this.checkoutCommand = checkoutCommand;
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

        Set<MenuItem> menuItems = menuController.getMenuItem(1);

        List<SelectOption> options = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            SelectOption option =
                    SelectOption.of(menuItem.getItemName(), String.valueOf(menuItem.getPrice()));
            options.add(option);
        }

        StringSelectMenu menu =
                StringSelectMenu.create("menu")
                        .setPlaceholder("Click to add a dish to your cart.")
                        .addOptions(options)
                        .build();

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(menu); // add menu to messageCreateBuilder
        // deleted three buttons

        hook.sendMessage(messageCreateBuilder.build()).queue();
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
        if (!response.equals("")) {

            Set<MenuItem> menuItems = menuController.getMenuItem(1);
            dishPrice =
                    menuItems.stream()
                            .filter(item -> item.getItemName().equals(response))
                            .findFirst()
                            .get()
                            .getPrice();
        }

        if (dishPrice != 0.0) {
            Dish dish = Dish.builder().dishName(response).price(dishPrice).build();
            // cart.addDish(dish);
            cartController.addToCart(dish, discordUserId);
            event.reply(reply).queue();
        }
    }

    // deleted button interaction
}
