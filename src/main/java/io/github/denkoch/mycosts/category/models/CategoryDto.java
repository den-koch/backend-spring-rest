package io.github.denkoch.mycosts.category.models;

import lombok.Value;

import java.util.UUID;

@Value
public class CategoryDto {
    private UUID id;
    private String label;
}
