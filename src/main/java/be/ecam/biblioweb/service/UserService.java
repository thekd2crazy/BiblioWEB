package be.ecam.biblioweb.service;

import be.ecam.biblioweb.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User addUser(String name, String surname, String email);

    void deleteUser(UUID id);

    User updateUser(UUID id, String name, String surname, String email);

    User getUser(UUID id);

    List<User> getAllUsers();
}
