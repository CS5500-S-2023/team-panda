package edu.northeastern.cs5500.starterbot.controller;

import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.MenuRepository;
import java.util.Set;
import javax.inject.Inject;

public class MenuController {
    @Inject MenuRepository menuRepository;

    public Set<MenuItem> getMenuItem(String id) {
        return menuRepository.findById(id).orElse(null).getMenuItems();
    }

    public void addMenuItem(String id, MenuItem menuItem) {
        menuRepository.findById(id).orElse(null).getMenuItems().add(menuItem);
    }
    
}
