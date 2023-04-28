package edu.northeastern.cs5500.starterbot.model;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
public class MenuItem implements Model {
    ObjectId id;

    @Nonnull private String itemName;
    private double price;
    private ObjectId menuId;

    @Inject
    public MenuItem() {}
}
