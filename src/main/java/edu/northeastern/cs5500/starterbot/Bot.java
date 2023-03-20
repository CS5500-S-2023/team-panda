/** This class contains the definition of the bot. */
package edu.northeastern.cs5500.starterbot;

import dagger.Component;
import edu.northeastern.cs5500.starterbot.command.CommandModule;
import edu.northeastern.cs5500.starterbot.listener.MessageListener;
import edu.northeastern.cs5500.starterbot.repository.RepositoryModule;
import java.util.Collection;
import java.util.EnumSet;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

// Bot component has 2 components: commandModule and repositoryModudle.
@Component(modules = {CommandModule.class, RepositoryModule.class})
@Singleton
interface BotComponent {
    public Bot bot();
}

public class Bot {
    // Use dagger for dependency injection. Everything gets injected with no explicit constructors.
    @Inject
    Bot() {} // class Bot has Bot component injected

    @Inject MessageListener messageListener;

    static String getBotToken() {
        return new ProcessBuilder().environment().get("BOT_TOKEN");
    }

    void start() {
        String token = getBotToken();
        if (token == null) {
            throw new IllegalArgumentException(
                    "The BOT_TOKEN environment variable is not defined.");
        }
        @SuppressWarnings("null")
        @Nonnull
        // intents tell discord what we need in order to work
        Collection<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
        // Invoke jda to build a connection based on the token and intents we want, and add a
        // messageListener
        JDA jda = JDABuilder.createLight(token, intents).addEventListeners(messageListener).build();
        // build our list of commands and queue them for later execution
        CommandListUpdateAction commands = jda.updateCommands();
        commands.addCommands(messageListener.allCommandData());
        commands.queue();
    }
}
