package io.github.denkoch.mycosts.category.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Category {
    private Long id;
    private String label;
    private Long userId;
}
