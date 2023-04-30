package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "MONGODB_URI", matches = ".+")
public class MenuItemControllerTest {
    static final String NAME = "rice";
    static final String PRICE = "0.99";
    static final ObjectId MENUID = new ObjectId("607e0a4f0d4d4c61b89a08ab");

    private MenuItemController getMenuItemController() {
        return new MenuItemController(new InMemoryRepository<>());
    }

    @Test
    public void testAddNewMenuItem() {
        MenuItemController menuItemController = getMenuItemController();
        assertThat(menuItemController.getMenuItems(MENUID)).isEmpty();
        MenuItem menuItem = menuItemController.addNewMenuItem(NAME, PRICE, MENUID);
        assertThat(menuItem.getItemName()).isEqualTo(NAME);
    }

    @Test
    public void testGetMenuItems() {
        MenuItemController menuItemController = getMenuItemController();
        assertThat(menuItemController.getMenuItems(MENUID)).isEmpty();

        menuItemController.addNewMenuItem(NAME, PRICE, MENUID);
        assertThat(menuItemController.getMenuItems(MENUID)).isNotEmpty();
    }
}
