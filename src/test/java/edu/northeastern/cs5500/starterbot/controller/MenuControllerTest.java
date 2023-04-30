package edu.northeastern.cs5500.starterbot.controller;

import static com.google.common.truth.Truth.assertThat;

import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;
import edu.northeastern.cs5500.starterbot.repository.InMemoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

@EnabledIfEnvironmentVariable(named = "MONGODB_URI", matches = ".+")
public class MenuControllerTest {
    static final String USER_ID_2 = "28484913146480";
    static final String NAME = "rice";
    static final String PRICE = "0.99";
    static final ObjectId MENUID = new ObjectId("607e0a4f0d4d4c61b89a08ac");

    private MenuController getMenuController() {
        return new MenuController(new InMemoryRepository<>());
    }

    private MenuItemController getMenuItemController() {
        return new MenuItemController(new InMemoryRepository<>());
    }

    @Test
    public void testGetMenuItems() {
        MenuController menuController = getMenuController();
        MenuItemController menuItemController = getMenuItemController();

        Menu menu = new Menu();
        menu.setOwnerId(USER_ID_2);

        assertThat(menuController.getMenuItems()).isEmpty();

        MenuItem menuItem = menuItemController.addNewMenuItem(NAME, PRICE, MENUID);
        assertThat(menuController.getMenuItems()).contains(menuItem);
    }

    @Test
    public void testIsMenuOwner() {
        MenuController menuController = getMenuController();
        Menu menu = new Menu();
        menu.setOwnerId(USER_ID_2);

        assertThat(menuController.isMenuOwner(USER_ID_2)).isTrue();
        assertThat(menuController.isMenuOwner("28484913146481")).isFalse();
    }

    @Test
    public void testAddMenuItem() {
        MenuItemController menuItemController = getMenuItemController();
        assertThat(menuItemController.getMenuItems(MENUID)).isEmpty();
        
        MenuItem menuItem = menuItemController.addNewMenuItem(NAME, PRICE, MENUID);
        assertThat(menuItem.getItemName()).isEqualTo(NAME);
        assertThat(menuItem.getId()).isEqualTo(MENUID);
    }
}
