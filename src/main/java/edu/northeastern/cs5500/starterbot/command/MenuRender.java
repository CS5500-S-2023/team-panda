package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.controller.MenuController;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

@Singleton
@Slf4j
public class MenuRender {
    @Inject MenuController menuController;

    @Inject
    public MenuRender() {
        // left blank for Dagger injection
    }

    public void renderMenu(@Nonnull InteractionHook hook) {
        log.info("event: /menu");

        Set<MenuItem> menuItems = menuController.getMenuItems();

        if (menuItems == null) {
            hook.sendMessage("No dish available.").queue();
            return;
        }

        StringSelectMenu.Builder menuBuilder =
                StringSelectMenu.create("menu").setPlaceholder("Click to add a dish to your cart.");
        for (MenuItem menuItem : menuItems) {
            menuBuilder.addOption(
                    menuItem.getItemName(),
                    menuItem.getItemName(),
                    "$" + String.valueOf(menuItem.getPrice()));
        }

        StringSelectMenu menu = menuBuilder.build();

        MessageCreateData messageCreateData = new MessageCreateBuilder().addActionRow(menu).build();
        hook.sendMessage(messageCreateData).queue();
    }
}
