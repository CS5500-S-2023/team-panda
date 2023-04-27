package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.MenuRepository;
import java.util.Set;
import javax.inject.Inject;

public class MenuController {
    @Inject MenuRepository menuRepository;

    @Inject
    public MenuController() {}

    public Set<MenuItem> getMenuItem(int id) {
        return menuRepository.findById(id).orElse(null).getMenuItems();
    }

    public void addMenuItem(int id, MenuItem menuItem) {
        menuRepository.findById(id).orElse(null).getMenuItems().add(menuItem);
    }

    public void createMenu(int id, Set<MenuItem> menuItems) {
        menuRepository.save(new Menu(id, menuItems));
    }
}
