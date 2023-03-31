package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
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
    private final Cart cart;
    private final MenuCommand menuCommand;

    @Inject
    public CartCommand(Cart cart, MenuCommand menuCommand) {
        this.cart = cart;
        this.menuCommand = menuCommand;
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

    private void displayCart(@Nonnull InteractionHook hook) {
        log.info("event: /cart");

        if (cart.getCart().isEmpty()) {
            hook.sendMessage("Your cart is empty.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your Cart");
        embedBuilder.setColor(0x1fab89); // You can change the color if you like

        double totalPrice = 0;
        for (Map.Entry<Dish, Integer> entry : cart.getCart().entrySet()) {
            Dish dish = entry.getKey();
            int quantity = entry.getValue();
            String itemName = String.format("%s (x%d)", dish.getDishName(), quantity);
            embedBuilder.addField(itemName, String.format("$%.2f", dish.getPrice()), true);
            totalPrice += dish.getPrice() * quantity;
        }
        embedBuilder.setFooter(String.format("Total Price: $%.2f", totalPrice));

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder =
                messageCreateBuilder.addActionRow(
                        Button.primary(this.getName() + ":add-more", "Add More"),
                        Button.primary(this.getName() + ":delete", "Delete"),
                        Button.success(this.getName() + ":checkout", "Checkout"),
                        Button.danger(this.getName() + ":cancel", "Cancel"));

        messageCreateBuilder.setContent("").setEmbeds(embedBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        displayCart(event.getHook());
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
                // To be finished
                break;
            case "checkout":
                // To be finished
                break;
            case "cancel":
                // Handle cancel action here
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    /**
     * Menu can be sent when somebody clicked a button.
     * @param event
     */
    public void sendCart(@Nonnull ButtonInteractionEvent event) {
        displayCart(event.getHook());
    }

    public Cart getCart() {
        return cart;
    }
}
