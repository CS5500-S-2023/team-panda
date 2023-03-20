package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public interface SlashCommandHandler {
    @Nonnull
    public String getName();

    /**
     * Gets what discord needs to know about this command.
     * @return CommandData and it cannot be null
     */
    @Nonnull
    public CommandData getCommandData();

    // get to know what to do when get a slash command interaction and where to send it
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event);
}
