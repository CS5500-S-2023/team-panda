package edu.northeastern.cs5500.starterbot.model;

import java.util.Set;
import javax.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
public class Menu implements Model {
    ObjectId id;

    private Set<MenuItem> menuItems;
    private String ownerId;

    @Inject
    public Menu() {}
}
