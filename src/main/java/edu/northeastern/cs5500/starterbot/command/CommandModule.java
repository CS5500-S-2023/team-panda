/** This CommendModule declares all the commands the bot provides */
package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.northeastern.cs5500.starterbot.model.Cart;
import javax.inject.Singleton;

@Module
public class CommandModule {

    @Provides // This is a provider that provides into a set of commands
    @IntoSet
    public SlashCommandHandler provideSayCommand(ButtonCommand sayCommand) {
        return sayCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler providePreferredNameCommand(
            PreferredNameCommand preferredNameCommand) {
        return preferredNameCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideButtonCommand(ButtonCommand buttonCommand) {
        return buttonCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideButtonCommandClickHandler(ButtonCommand buttonCommand) {
        return buttonCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDropdownCommand(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideDropdownCommandMenuHandler(DropdownCommand dropdownCommand) {
        return dropdownCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideStartCommand(StartCommand startCommand) {
        return startCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideStartCommandMenuHandler(StartCommand startCommand) {
        return startCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideMenuCommand(MenuCommand menuCommand) {
        return menuCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideMenuCommandMenuHandler(MenuCommand menuCommand) {
        return menuCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCartCommand(CartCommand cartCommand) {
        return cartCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideCartCommandClickHandler(CartCommand cartCommand) {
        return cartCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCheckoutCommand(CheckoutCommand checkoutCommand) {
        return checkoutCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideCheckoutCommandClickHandler(CheckoutCommand checkoutCommand) {
        return checkoutCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCongraCommand(CongraCommand congraCommand) {
        return congraCommand;
    }

    @Provides
    @Singleton
    public Integer provideInteger(Cart cart) {
        return cart.getOrderNumber();
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideQueueCommand(QueueCommand queueCommand) {
        return queueCommand;
    }
}
