package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ButtonHandler {
    @Nonnull
    public String getName();
    // get to know what to do when get a button interaction and where to send it
    public void onButtonInteraction(@Nonnull ButtonInteractionEvent event);
}
