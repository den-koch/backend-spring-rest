package io.github.denkoch.mycosts.config.mappers;

import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.models.UserCreationDto;
import io.github.denkoch.mycosts.user.models.UserDto;

public class UserMapper {
    public User dtoToUser(UserCreationDto userCreationDto) {
        return new User(null, userCreationDto.getName(),
                userCreationDto.getEmail(), userCreationDto.getPassword());
    }

    public UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
