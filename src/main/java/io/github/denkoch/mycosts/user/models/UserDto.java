package io.github.denkoch.mycosts.user.models;

import lombok.Value;

import java.util.UUID;

@Value
public class UserDto {
    private UUID id;
    private String name;
    private String email;
}
