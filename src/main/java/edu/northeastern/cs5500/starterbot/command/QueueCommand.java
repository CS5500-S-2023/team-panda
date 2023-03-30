/** This represents a QueueCommmand whichi implements SlashCoomandHandler. */
package edu.northeastern.cs5500.starterbot.command;

import edu.northeastern.cs5500.starterbot.model.Order;
import edu.northeastern.cs5500.starterbot.model.Status;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

@Singleton
@Slf4j
public class QueueCommand implements SlashCommandHandler {

    private final List<Order> ordersInProgress = new ArrayList<>();
    private final List<Order> readyOrders = new ArrayList<>();

    @Inject
    public QueueCommand() {}

    @Override
    @Nonnull
    public String getName() {
        return "viewqueue";
    }

    @Override
    @Nonnull
    public CommandData getCommandData() {
        return Commands.slash(getName(), "View the current queue");
    }
    /**
     * Display a embedBuilder containing the order conditions.
     *
     * @return None
     * @param hook
     */
    private void display(@Nonnull InteractionHook hook) {
        log.info("event: /viewqueue");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Queue");

        if (ordersInProgress.isEmpty() && readyOrders.isEmpty()) {
            embedBuilder.setDescription("There are no orders in the queue.");
        } else {
            if (!ordersInProgress.isEmpty()) {
                embedBuilder.addField("In Progress", getOrdersAsString(ordersInProgress), false);
            }
            if (!readyOrders.isEmpty()) {
                embedBuilder.addField("Ready", getOrdersAsString(readyOrders), false);
            }
        }

        hook.sendMessageEmbeds(embedBuilder.build()).setEphemeral(true).queue();
    }

    private String getOrdersAsString(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        for (Order order : orders) {
            sb.append("Order #").append(order.getOrderNumber()).append(" - ");
            sb.append(order.getStatus()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        display(event.getHook());
    }

    public void sendQueue(@Nonnull StringSelectInteractionEvent event) {
        display(event.getHook());
    }

    public void orderReady(int orderNumber) {
        Order order =
                ordersInProgress.stream()
                        .filter(o -> o.getOrderNumber() == orderNumber)
                        .findFirst()
                        .orElse(null);
        if (order != null) {
            order.setStatus(Status.READY);
            ordersInProgress.remove(order);
            readyOrders.add(order);
        }
    }

    public int addOrderInProgress(Order order) {
        order.setStatus(Status.IN_PROGRESS);
        int orderNumber = ordersInProgress.size() + 1;
        order.setOrderNumber(orderNumber);
        ordersInProgress.add(order);
        return orderNumber;
    }

    public List<Order> getOrdersInProgress() {
        return ordersInProgress;
    }

    public List<Order> getReadyOrders() {
        return readyOrders;
    }
}