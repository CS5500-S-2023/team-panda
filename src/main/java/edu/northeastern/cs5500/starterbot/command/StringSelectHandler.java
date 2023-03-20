package edu.northeastern.cs5500.starterbot.command;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;

public interface StringSelectHandler {
    @Nonnull
    public String getName();

    // get to know what to do when get a string select interaction and where to send it
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event);
}
