package io.github.denkoch.mycosts.category.models;

import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.models.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.UUID;

@Data
@NoArgsConstructor
public class CategoryDto {
    private UUID id;
    private String label;
//    private UserDto user;
}
