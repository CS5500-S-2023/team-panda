package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

@Singleton
@Slf4j
public class CartCommand implements SlashCommandHandler, ButtonHandler {
    // Substituted a cart with a cartController
    private final CartController cartController;
    private final MenuCommand menuCommand;
    private final Provider<MenuCommand> menuCommandProvider;
    private final Provider<CheckoutCommand> checkoutCommandProvider;
    private final Provider<DeleteCommand> deleteCommandProvider;

    @Inject
    public CartCommand(
            // Cart cart,
            CartController cartController,
            MenuCommand menuCommand,
            Provider<MenuCommand> menuCommandProvider,
            Provider<CheckoutCommand> checkoutCommandProvider,
            Provider<DeleteCommand> deleteCommandProvider) {
        // this.cart = cart;
        this.cartController = cartController;
        this.menuCommand = menuCommand;
        this.menuCommandProvider = menuCommandProvider;
        this.checkoutCommandProvider = checkoutCommandProvider;
        this.deleteCommandProvider = deleteCommandProvider;
    }

    @Override
    @Nonnull
    public String getName() {
        return "cart";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Display all dishes added to the cart.");
    }

    /**
     * If the cart is empty, display the total price as 0 and provide the user options to menu and
     * cancel. If there is something in the cart, list them and display the total price and provide
     * the user options to add more, delete, checkout, and cancel.
     *
     * @param hook
     */
    public void displayCart(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /cart");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Cart");
        embedBuilder.setColor(0x1fab89);

        double totalPrice = 0;
        if (!cartController.getItemsInCart(discordUserId).isEmpty()) {
            for (Map.Entry<Dish, Integer> entry :
                    // use cartController instead of Cart to get specific cart for the user.
                    cartController.getItemsInCart(discordUserId).entrySet()) {
                Dish dish = entry.getKey();
                int quantity = entry.getValue();
                String itemName = String.format("%s (x%d)", dish.getDishName(), quantity);
                embedBuilder.addField(itemName, String.format("$%.2f", dish.getPrice()), false);
                totalPrice += dish.getPrice() * quantity;
            }
        } else {
            embedBuilder.setDescription("Your cart is empty.");
        }
        embedBuilder.setFooter(String.format("Total Price: $%.2f", totalPrice));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        if (!cartController.getItemsInCart(discordUserId).isEmpty()) {
            messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                            Button.primary(this.getName() + ":add-more", "Add More"),
                            Button.primary(this.getName() + ":delete", "Delete"),
                            Button.success(this.getName() + ":checkout", "Checkout"),
                            Button.danger(this.getName() + ":cancel", "Cancel"));
        } else {
            messageCreateBuilder =
                    messageCreateBuilder.addActionRow(
                            Button.primary(this.getName() + ":add-more", "Add more"),
                            Button.danger(this.getName() + ":cancel", "Cancel"));
        }

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        displayCart(event.getHook(), discordUserId);
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String response = event.getButton().getId().split(":")[1];
        event.deferReply().queue();
        switch (response) {
            case "add-more":
                menuCommand.sendMenu(event);
                break;
            case "delete":
                deleteCommandProvider.get().sendDeletePage(event);
                break;
            case "checkout":
                checkoutCommandProvider.get().sendCheckout(event);
                break;
            case "cancel":
                clearCart(event);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    /**
     * Menu can be sent when somebody clicked a button.
     *
     * @param event
     */
    public void sendCart(@Nonnull ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        displayCart(event.getHook(), discordUserId);
    }

    // public Cart getCart() {
    //     return cartController.getCart();
    // }

    public void clearCart(ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        cartController.clearCart(discordUserId);
        event.getHook()
                .sendMessage("Your cart has been cleared. Thanks for visiting FoodiePanda.")
                .queue();
    }
}
