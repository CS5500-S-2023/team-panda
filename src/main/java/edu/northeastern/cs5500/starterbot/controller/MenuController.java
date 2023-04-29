package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Set;
import javax.inject.Inject;

public class MenuController {
    GenericRepository<Menu> menuRepository;
    GenericRepository<MenuItem> menuItemRepository;
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
        Menu menu = getMenu();
        Set<MenuItem> output = menuItemController.getMenuItems(menu.getId());
        if (output.isEmpty()) {
            return null;
        } else {
            return output;
        }
    }

    /**
     * Get the menu. We design to have only one menu for the panda restauant. so we by default get
     * the first and only menu.
     *
     * @return
     */
    public Menu getMenu() {
        return menuRepository.getAll().iterator().next();
    }

    /**
     * Add menuItem to our menu.
     *
     * @param menuItem
     */
    public void addMenuItem(MenuItem menuItem) {
        Menu menu = getMenu();
        menu.addMenuItem(menuItem);
        menuRepository.update(menu);
        menuItemRepository.add(menuItem);
    }

    // TODO Add getMenuItemsByMenuId(String menuId) if we figure out how to get menuId
}
