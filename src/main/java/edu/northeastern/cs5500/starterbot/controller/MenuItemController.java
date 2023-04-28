package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import org.bson.types.ObjectId;

public class MenuItemController {
    GenericRepository<MenuItem> menuItemRepository;

    @Inject
    MenuItemController(GenericRepository<MenuItem> menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public Set<MenuItem> getMenuItems(ObjectId menuId) {
        Set<MenuItem> items = new HashSet<MenuItem>();
        Collection<MenuItem> menuItems = menuItemRepository.getAll();
        for (MenuItem menuItem : menuItems) {
            if (menuItem.getMenuId().equals(menuId)) {
                items.add(menuItem);
            }
        }
        return items;
    }
}