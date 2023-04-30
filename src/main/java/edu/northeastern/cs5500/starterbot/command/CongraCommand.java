package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.CartController;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.inject.Inject;
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
    @Inject CartController cartController;
    @Inject PickUpCommand pickUpCommand;
    @Inject DeliveryCommand deliveryCommand;

    @Inject
    public CongraCommand() {
        // left blank for Dagger injection
    }

    /** The name of this class. */
    @Override
    @Nonnull
    public String getName() {
        return "congra";
    }

    /** The description of this command. */
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
     * @param discordUserId the discord Id of user
     */
    private void display(@Nonnull InteractionHook hook, String discordUserId) {
        log.info("event: /congra");
        cartController.clearCart(discordUserId);
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Congratulation!");
        embedBuilder.setColor(0x1fab89);
        embedBuilder.setDescription("Your order has been placed.");

        hook.sendMessageEmbeds(embedBuilder.build())
                .addActionRow(
                        Button.primary(getName() + ":pickup", "Pick up"),
                        Button.primary(getName() + ":deliver", "Deliver"))
                .setEphemeral(true)
                .queue();
    }

    /** Shown the congratulation message to customer. */
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        display(event.getHook(), discordUserId);
    }

    /**
     * Send cart to the customer. They can see what they have added to their cart.
     *
     * @param event
     */
    public void sendCongra(@Nonnull StringSelectInteractionEvent event) {
        String discordUserId = event.getUser().getId();
        display(event.getHook(), discordUserId);
    }

    /**
     * Send cart to the customer. They can see what they have added to their cart.
     *
     * @param event
     */
    public void sendCongra(@Nonnull ButtonInteractionEvent event) {
        String discordUserId = event.getUser().getId();
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
        String id = Objects.requireNonNull(event.getButton().getId());
        String action = id.split(":", 2)[1];
        Objects.requireNonNull(action);

        event.deferReply().queue();

        switch (action) {
            case "pickup":
                pickUpCommand.sendPickup(event);
                break;
            case "deliver":
                deliveryCommand.sendDeliver(event);
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }
}
