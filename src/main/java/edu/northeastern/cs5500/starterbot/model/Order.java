package edu.northeastern.cs5500.starterbot.model;

import java.time.LocalDateTime;
import javax.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private int orderNumber;
    @Nonnull private LocalDateTime orderTime;
    @Nonnull private Status status;
}
