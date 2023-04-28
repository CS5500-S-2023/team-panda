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
    // NOTE: You can use the following lines if you'd like to store objects in memory.
    // NOTE: The presence of commented-out code in your project *will* result in a lowered grade.

    // @Provides
    // public GenericRepository<UserPreference> provideUserPreferencesRepository(
    //         MongoDBRepository<UserPreference> repository) {
    //     return repository;
    // }

    // Equivalent with the following, but this one is using mongoDB.
    // Professor said he prefers the above one because it is less hard-coding.
    // @Provides
    // public GenericRepository<UserPreference> provideUserPreferencesRepository (
    //     MongoDBService mongoDBService
    // ) {
    //     return new MongoDBRepository<>(UserPreference.class, mongoDBService)
    // }

    // @Provides
    // public Class<UserPreference> provideUserPreference() {
    //     return UserPreference.class;
    // }

    @Provides
    public GenericRepository<Cart> provideCartRepository(InMemoryRepository<Cart> repository) {
        return repository;
    }

    // @Provides
    // public GenericRepository<Menu> provideMenuRepository(InMemoryRepository<Menu> repository) {
    //     return repository;
    // }

    @Provides
    public GenericRepository<Menu> provideMenuRepository(MongoDBRepository<Menu> repository) {
        return repository;
    }

    // @Provides
    // public GenericRepository<MenuItem> provideMenuItemRepository(
    //         InMemoryRepository<MenuItem> repository) {
    //     return repository;
    // }

    @Provides
    public GenericRepository<MenuItem> provideMenuItemRepository(
            MongoDBRepository<MenuItem> repository) {
        return repository;
    }
}
