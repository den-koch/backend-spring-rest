package io.github.denkoch.mycosts.user.repositories;

import io.github.denkoch.mycosts.user.models.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private Map<UUID, User> repository = new TreeMap<>();

    @Override
    public User findById(UUID id) {
        return repository.get(id);
    }

    @Override
    public Collection<User> findAll() {
        return repository.values();
    }

    @Override
    public User save(User user) {
        repository.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(UUID id) {
        repository.remove(id);
    }
}
