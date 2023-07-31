package com.restapi.scoregoat.service.DBService;

import com.restapi.scoregoat.domain.*;
import com.restapi.scoregoat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class UserDBService {
    private final UserRepository repository;

    public boolean existsById(long id) {
        return repository.existsById(id);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public User findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public User findByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    public User findByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public boolean deleteById(long id) {
        repository.deleteById(id);
        return true;
    }

    public boolean nameExistCheck(Long userId, String name) {
        List<User> usersWithEmail = repository.findAllByName(name).stream().filter(
                user -> !user.getId().equals(userId)).toList();
        return usersWithEmail.size() == 0;
    }
    public boolean emailExistCheck(Long userId, String email) {
        List<User> usersWithEmail = repository.findAllByEmail(email).stream().filter(
                user -> !user.getId().equals(userId)).toList();
        return usersWithEmail.size() == 0;
    }
}
