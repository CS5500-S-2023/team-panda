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
public class PickUpCommand implements SlashCommandHandler, ButtonHandler {
    private final MenuCommand menuCommand;

    @Inject
    public PickUpCommand(MenuCommand menuCommand) {
        this.menuCommand = menuCommand;
    }

    @Override
    @Nonnull
    public String getName() {
        return "pickup";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "Pick up your order.");
    }

    private void display(@Nonnull InteractionHook hook) {
        log.info("event: /pickup");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Your order is ready to pick up.");

        hook.sendMessageEmbeds(embedBuilder.build())
                .addActionRow(
                        Button.primary(getName() + ":menu", "Place new order"),
                        Button.danger(getName() + ":exit", "Exit"))
                .setEphemeral(true)
                .queue();
    }

    @Override
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event) {
        final String response = event.getButton().getId().split(":")[1];
        Objects.requireNonNull(response);
        event.deferReply()
                .queue(); // This sentence generated by ChatGPT using OpenAI GPT-3.5 model, version
        // 2021-09
        if (response.equals("menu")) {
            menuCommand.sendMenu(event);
        } else if (response.equals("exit")) {
            event.getHook().sendMessage("Thanks for your order. See you next time!").queue();
        } else {
            event.getHook().sendMessage("Invalid option selected.").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        display(event.getHook());
    }

    public void sendPickup(@Nonnull ButtonInteractionEvent event) {
        display(event.getHook());
    }
}