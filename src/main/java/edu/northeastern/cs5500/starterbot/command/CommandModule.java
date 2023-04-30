/** This CommendModule declares all the commands the bot provides */
package edu.northeastern.cs5500.starterbot.command;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;
import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;

@Module
public class CommandModule {

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
    public SlashCommandHandler providePickUpCommand(PickUpCommand pickupCommand) {
        return pickupCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler providePickUpCommandClickHandler(PickUpCommand pickupCommand) {
        return pickupCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideDeliveryCommand(DeliveryCommand deliveryCommand) {
        return deliveryCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideDeliveryCommandClickHandler(DeliveryCommand deliveryCommand) {
        return deliveryCommand;
    }

    @Provides
    @IntoSet
    public StringSelectHandler provideDeleteCommandClickHandler(DeleteCommand deleteCommand) {
        return deleteCommand;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideCongraCommand(CongraCommand congraCommand) {
        return congraCommand;
    }

    @Provides
    @IntoSet
    public ButtonHandler provideCongraCommandClickHandler(CongraCommand congraCommand) {
        return congraCommand;
    }

    @Provides
    public Class<MenuItem> provideMenuItem() {
        return MenuItem.class;
    }

    @Provides
    public Class<Menu> provideMenu() {
        return Menu.class;
    }

    @Provides
    @IntoSet
    public SlashCommandHandler provideAddMenuItemCommand(AddMenuItemCommand addMenuItemCommand) {
        return addMenuItemCommand;
    }
}
