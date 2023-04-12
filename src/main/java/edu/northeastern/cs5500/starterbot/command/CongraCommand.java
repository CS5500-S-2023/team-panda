package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import edu.northeastern.cs5500.starterbot.model.Cart;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@Singleton
@Slf4j
public class CongraCommand implements SlashCommandHandler, ButtonHandler {
    // Substituted a cart with a cartController
    private final CartController cartController;
    private Integer orderNumber;
    private final Provider<MenuCommand> menuCommandProvider;

    @Inject
    public CongraCommand(CartController cartController, Provider<MenuCommand> menuCommandProvider) {
        this.cartController = cartController;
        this.menuCommandProvider = menuCommandProvider;
        // this.orderNumber = cart.getOrderNumber();
    }

    @Override
    @Nonnull
    public String getName() {
        return "congra";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Checkout successfully!");
    }

    /**
     * Displays the message of order completed after the user make payment, along with two buttons
     * for user to choose as either to pick or deliver the order.
     *
     * @param hook
     * @param discordUserId
     */
    private void display(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /congra");

        // cart.clear();
        Integer orderNumber = cartController.getCartForUser(discordUserId).getOrderNumber();
        cartController.clearCart(discordUserId);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Congratulation!");
        embedBuilder.setColor(0x1fab89);
        embedBuilder.addField(
                "Your order has been placed.",
                "Order number: " + String.valueOf(orderNumber),
                true);

        hook.sendMessageEmbeds(embedBuilder.build())
                .addActionRow(
                        Button.primary(getName() + ":pickup", "Pick up"),
                        Button.primary(getName() + ":deliver", "Deliver"))
                .setEphemeral(true)
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        display(event.getHook(), discordUserId);
    }

    public void sendCongra(@Nonnull StringSelectInteractionEvent event, int number) {
        orderNumber = number;
        String discordUserId = event.getUser().getId();
        Cart cart =
                cartController.getCartForUser(
                        discordUserId); // set order number using cartController not cart
        cart.setOrderNumber(number);
        display(event.getHook(), discordUserId);
    }

    public void sendCongra(@Nonnull ButtonInteractionEvent event, int number) {
        orderNumber = number;
        String discordUserId = event.getUser().getId();
        Cart cart = cartController.getCartForUser(discordUserId);
        cart.setOrderNumber(number);
        display(event.getHook(), discordUserId);
    }

    /**
     * The use by clicking on the button "pick up", it displays the message "Your order is ready to
     * pick up.", and by clicking on the button "deliver", it will display "Your order is
     * delivered."
     *
     * @param event
     */
    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        String action = id.split(":", 2)[1];
        Objects.requireNonNull(action);
        PickUpCommand pickUpCommand = new PickUpCommand(menuCommandProvider.get());
        DeliveryCommand deliveryCommand = new DeliveryCommand(menuCommandProvider.get());

        event.deferReply().queue();

        switch (action) {
            case "pickup":
                // event.reply("Your order is ready to pick up.").setEphemeral(true).queue();
                pickUpCommand.sendPickup(event);
                break;
            case "deliver":
                // event.reply("Your order is delivered.").setEphemeral(true).queue();
                deliveryCommand.sendDeliver(event);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }
}
