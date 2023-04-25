package edu.northeastern.cs5500.starterbot.model;

import java.util.Map;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Receipt implements Model {
    ObjectId id;

    @Nonnull private ObjectId userId;
    @Nonnull private Map<Dish, Integer> dishes;
}
