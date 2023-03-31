package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
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

@Singleton
@Slf4j
public class CongraCommand implements SlashCommandHandler {
    private final Cart cart;
    private Integer orderNumber;

    @Inject
    public CongraCommand(Cart cart) {
        this.cart = cart;
        this.orderNumber = cart.getOrderNumber();
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

    private void display(@Nonnull InteractionHook hook) {
        log.info("event: /congra");

        cart.clear();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Congratulation!");
        embedBuilder.setColor(0x1fab89);
        embedBuilder.addField(
                "Your order has been placed.",
                "Order number: " + String.valueOf(orderNumber),
                true);

        hook.sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        display(event.getHook());
    }

    public void sendCongra(@Nonnull StringSelectInteractionEvent event, int number) {
        orderNumber = number;
        display(event.getHook());
    }

    public void sendCongra(@Nonnull ButtonInteractionEvent event, int number) {
        orderNumber = number;
        display(event.getHook());
    }
}
