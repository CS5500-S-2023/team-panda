package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class CongraCommand implements SlashCommandHandler {
    private final Cart cart;
    private Integer number;

    @Inject
    public CongraCommand(Integer number, Cart cart) {
        this.number = number;
        this.cart = cart;
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

        number = 10;
        cart.clear();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Congratulation!");
        embedBuilder.setColor(0x1fab89);
        embedBuilder.addField("Your order number is: ", String.valueOf(number), true);

        hook.sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        display(event.getHook());
    }

    public void sendCongra(@Nonnull StringSelectInteractionEvent event) {
        display(event.getHook());
    }
}
