package edu.northeastern.cs5500.starterbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Menu implements Model {
    ObjectId id;

    String ownerId;
}
