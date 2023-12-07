package io.github.denkoch.mycosts.user.services;

import io.github.denkoch.mycosts.user.models.User;
import io.github.denkoch.mycosts.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Collection<User> readUsers(){
        return userRepository.findAll();
    }

    public User readUser(UUID id){
        return userRepository.findById(id);
    }

    public User createUser(User user){
        UUID id = UUID.randomUUID();
        user.setId(id);
        return userRepository.save(user);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public void deleteUser(UUID id){
        userRepository.deleteById(id);
    }
}
