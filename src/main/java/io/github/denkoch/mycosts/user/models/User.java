package io.github.denkoch.mycosts.user.models;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
}
