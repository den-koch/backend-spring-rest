package io.github.denkoch.mycosts.category.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.UUID;

@Value
public class CategoryCreationDto {
    @NotBlank
    private String label;
    @NotNull
    private Long userId;
}
