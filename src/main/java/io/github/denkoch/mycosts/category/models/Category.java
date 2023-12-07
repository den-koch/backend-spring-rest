package io.github.denkoch.mycosts.category.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Category {
    private UUID id;
    private String label;
    private UUID userId;
}
