package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class SayCommand implements SlashCommandHandler {

    @Inject
    public SayCommand() {
        // Defined public and empty for Dagger injection
    }

    @Override
    @Nonnull
    public String getName() {
        return "say";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        // get the name and descriptions of the command
        return Commands.slash(getName(), "Ask the bot to reply with the provided text")
                .addOption(
                        OptionType.STRING,
                        "content", // the name of the command option
                        "The bot will reply to your command with the provided text", // what the option does
                        true); // it's required. We can add more .addOption()
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        log.info("event: /say"); // get called when type /say
        var option = event.getOption("content");
        if (option == null) {
            log.error("Received null value for mandatory parameter 'content'");
            return;
        }
        event.reply(option.getAsString()).queue();
    }
}
