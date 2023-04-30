package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;

public class MenuController {
    GenericRepository<Menu> menuRepository;
    @Inject MenuItemController menuItemController;

    @Inject
    MenuController(GenericRepository<Menu> menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * Get all menuItems. I am no using parameters, since we only have one menu.
     *
     * @return a set of menuItems
     */
    public Set<MenuItem> getMenuItems() {
        Menu menu = menuRepository.getAll().iterator().next();
        Set<MenuItem> output = menuItemController.getMenuItems(menu.getId());
        if (output.isEmpty()) {
            return Collections.emptySet();
        } else {
            return output;
        }
    }

    public boolean isMenuOwner(String discordUserId) {
        Menu menu = menuRepository.getAll().iterator().next();
        return menu.getOwnerId().equals(discordUserId);
    }

    /**
     * Add menuItem into Menu. If this itemName already in the database, it will return false. If
     * added successfully, it will return true.
     *
     * @param itemName The name of the item
     * @param itemPrice the price of the item
     * @return true if added, false otherwise
     */
    public boolean addMenuItem(String itemName, String itemPrice) {
        boolean added = false;
        Menu menu = menuRepository.getAll().iterator().next();
        for (MenuItem item : this.getMenuItems()) {
            if (item.getItemName().equals(itemName)) {
                return false;
            }
        }
        Objects.requireNonNull(itemName);
        MenuItem menuItem = menuItemController.addNewMenuItem(itemName, itemPrice, menu.getId());
        for (MenuItem item : this.getMenuItems()) {
            if (item.getId().equals(menuItem.getId())) {
                added = true;
            }
        }
        return added;
    }
}
