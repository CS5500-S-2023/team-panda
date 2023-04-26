package edu.northeastern.cs5500.starterbot.command;

import java.util.Objects;
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

@Singleton
@Slf4j
public class DeliveryCommand implements SlashCommandHandler, ButtonHandler {
    @Inject MenuCommand menuCommand;

    @Inject
    public DeliveryCommand() {
        // left blank for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return "deliver";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Deliver your order.");
    }

    private void display(@Nonnull InteractionHook hook) {
        log.info("event: /deliver");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your order is delivered.");

        hook.sendMessageEmbeds(embedBuilder.build())
                .addActionRow(
                        Button.primary(getName() + ":menu", "Place new order"),
                        Button.danger(getName() + ":exit", "Exit"))
                .setEphemeral(true)
                .queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String id = Objects.requireNonNull(event.getButton().getId());
        final String response = id.split(":")[1];
        Objects.requireNonNull(response);
        event.deferReply().queue();
        switch (response) {
            case "menu":
                menuCommand.sendMenu(event);
                break;
            case "exit":
                event.getHook().sendMessage("Thanks for your order. See you next time!").queue();
                break;
            default:
                event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        display(event.getHook());
    }

    public void sendDeliver(@Nonnull ButtonInteractionEvent event) {
        display(event.getHook());
    }
}
