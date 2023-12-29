package io.github.denkoch.mycosts.user.repositories;

import io.github.denkoch.mycosts.user.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.UUID;

public interface UserRepository extends ListCrudRepository<User, UUID> {
}
