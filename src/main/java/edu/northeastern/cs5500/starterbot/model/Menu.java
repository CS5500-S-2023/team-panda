package edu.northeastern.cs5500.starterbot.model;

import lombok.Data;

import java.awt.MenuItem;
import java.util.Set;

import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Model {
    private Set<MenuItem> menuItems;
    ObjectId id;

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public boolean delteMenuItem(MenuItem menuItem) {
        if (menuItems.contains(menuItem)) {
            menuItems.remove(menuItem); 
            return True;
        } 
        return False;
    }

}