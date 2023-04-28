package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
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
            return null;
        } else {
            return output;
        }
    }

    // TODO Add getMenuItemsByMenuId(String menuId) if we figure out how to get menuId
}
