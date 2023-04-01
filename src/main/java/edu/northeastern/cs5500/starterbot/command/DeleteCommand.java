package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Dish;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

/**
 * This is a delete handler that handles the delete operation of a dish from the cart. When a user
 * clicks on the delete button from the cart, user gets directed to the delete handler to choose a
 * dish to delete from the dropdown. Once a dish is clicked, it will be delete from the cart and the
 * user will be redirected back to the cart.
 */
@Singleton
@Slf4j
public class DeleteCommand implements StringSelectHandler {

    private final Cart cart;
    private final Provider<CartCommand> cartCommandProvider;

    /**
     * The delete command takes the values form the cart.
     *
     * @param cart
     * @param cartCommandProvider
     */
    @Inject
    public DeleteCommand(Cart cart, Provider<CartCommand> cartCommandProvider) {
        this.cart = cart;
        this.cartCommandProvider = cartCommandProvider;
    }

    /** The delete handler is accessed by the button named "delete". */
    @Nonnull
    public String getName() {
        return "delete";
    }

    /**
     * Displays the delete dropdown with the dish options from the cart. The dish options will
     * contain the counts of each dish. Once deleted, all the same dishes would be deleted.
     *
     * @param hook
     */
    private void displayDeletePage(@Nonnull InteractionHook hook) {
        log.info("event: /delete");

        if (cart.getCart().isEmpty()) {
            hook.sendMessage("Your cart is empty.").setEphemeral(true).queue();
            return;
        }

        StringSelectMenu.Builder menuBuilder =
                StringSelectMenu.create(getName())
                        .setPlaceholder("Click to remove a dish from your cart.");

        for (Map.Entry<Dish, Integer> entry : cart.getCart().entrySet()) {
            Dish dish = entry.getKey();
            int count = entry.getValue();
            String dishWithCount = String.format("%s (x%d)", dish.getDishName(), count);
            menuBuilder.addOption(dishWithCount, dish.getDishName());
        }

        MessageCreateBuilder messageCreateBuilder = new MessageCreateBuilder();
        messageCreateBuilder = messageCreateBuilder.addActionRow(menuBuilder.build());
        hook.sendMessage(messageCreateBuilder.build()).queue();
    }

    /**
     * Other handlers are able to invoke the displayDeletePage method by calling this method.
     *
     * @param event
     */
    public void sendDeletePage(@Nonnull ButtonInteractionEvent event) {
        displayDeletePage(event.getHook());
    }

    /**
     * Implement the delete action. Once the user clicked on the dish, a delete success message
     * would be displayed to inform the user and the cart will show automatically to the user for
     * the following handlings.
     */
    @Override
    public void onStringSelectInteraction(@Nonnull StringSelectInteractionEvent event) {
        String selectedDishName = event.getInteraction().getValues().get(0);
        Dish dishToRemove = null;

        for (Dish dish : cart.getCart().keySet()) {
            if (dish.getDishName().equals(selectedDishName)) {
                dishToRemove = dish;
                break;
            }
        }

        if (dishToRemove != null) {
            cart.deleteDish(dishToRemove);
            event.reply("You removed " + selectedDishName + " from your cart.").queue();
            cartCommandProvider.get().displayCart(event.getHook());
        } else {
            event.reply("Invalid dish name.").queue();
        }
    }
}
