package io.github.denkoch.mycosts.user.repositories;

import io.github.denkoch.mycosts.user.models.User;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository {
    User findById(Long id);
    Collection<User> findAll();

    User save(User user);

    void deleteById(Long id);
}
