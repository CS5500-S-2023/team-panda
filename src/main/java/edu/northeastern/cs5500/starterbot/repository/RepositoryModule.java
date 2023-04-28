/**
 * This repsents a repository module in which we can choose whether we want to store objects in
 * memory or in a database. We can do unit tests with InMemoryRepository only, since they have do
 * the same thing
 */
package edu.northeastern.cs5500.starterbot.repository;

import dagger.Module;
import dagger.Provides;
import edu.northeastern.cs5500.starterbot.model.Cart;
import edu.northeastern.cs5500.starterbot.model.Menu;
import edu.northeastern.cs5500.starterbot.model.MenuItem;

@Module
public class RepositoryModule {

    @Provides
    public GenericRepository<Cart> provideCartRepository(InMemoryRepository<Cart> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<Menu> provideMenuRepository(MongoDBRepository<Menu> repository) {
        return repository;
    }

    @Provides
    public GenericRepository<MenuItem> provideMenuItemRepository(
            MongoDBRepository<MenuItem> repository) {
        return repository;
    }
}
