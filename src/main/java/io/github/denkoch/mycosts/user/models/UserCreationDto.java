package io.github.denkoch.mycosts.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class UserCreationDto {
    @NotBlank
    private String name;
    @Email
    private String email;
    @NotNull
    private String password;
}
