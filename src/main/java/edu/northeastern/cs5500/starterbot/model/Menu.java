package edu.northeastern.cs5500.starterbot.model;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    ObjectId id;
    Set<MenuItem> menuItems;
}
