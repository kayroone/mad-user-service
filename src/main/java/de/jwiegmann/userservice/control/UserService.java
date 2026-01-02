package de.jwiegmann.userservice.control;

import de.jwiegmann.userservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    User update(Long id, User user);

    void delete(Long id);
}
