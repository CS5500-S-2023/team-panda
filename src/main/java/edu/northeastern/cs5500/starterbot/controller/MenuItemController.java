package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.GenericRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
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

    public MenuItem addNewMenuItem(@Nonnull String itemName, String itemPrice, ObjectId menuId) {
        MenuItem menuItem =
                MenuItem.builder()
                        .id(new ObjectId())
                        .itemName(itemName)
                        .price(Double.parseDouble(itemPrice))
                        .menuId(menuId)
                        .build();
        Objects.requireNonNull(menuItem);
        menuItemRepository.add(menuItem);
        return menuItem;
    }
}
