package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import javax.inject.Inject;

public class MenuController {
    GenericRepository<MenuItem> menuRepository;

    @Inject
    MenuController(GenericRepository<MenuItem> menuRepository) {
        this.menuRepository = menuRepository;
    }

    public MenuItem getMenuItemByName(String itemName) {
        Collection<MenuItem> menuItems = menuRepository.getAll();
        for (MenuItem currentItem : menuItems) {
            if (currentItem.getItemName().equals(itemName)) {
                return currentItem;
            }
        }
        return null;
    }
}
