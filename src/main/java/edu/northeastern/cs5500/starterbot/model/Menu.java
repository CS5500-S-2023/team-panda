package edu.northeastern.cs5500.starterbot.model;

import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class Menu implements Model {
    ObjectId id;
    Set<MenuItem> menuItems;
}
