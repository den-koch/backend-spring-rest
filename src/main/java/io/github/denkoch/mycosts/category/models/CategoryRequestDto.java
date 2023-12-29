package io.github.denkoch.mycosts.category.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Value;


@Data
public class CategoryRequestDto {

    @NotBlank
    private String label;
}
