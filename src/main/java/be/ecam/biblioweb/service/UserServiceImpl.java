package be.ecam.biblioweb.service;

import be.ecam.biblioweb.exception.EmailAlreadyUsedException;
import be.ecam.biblioweb.exception.ResourceNotFoundException;
import be.ecam.biblioweb.model.User;
import be.ecam.biblioweb.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(String name, String surname, String email) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new EmailAlreadyUsedException("Email deja utilise : " + email);
        });
        return userRepository.save(new User(name, surname, email));
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(getUser(id));
    }

    @Override
    public User updateUser(UUID id, String name, String surname, String email) {
        User user = getUser(id);
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUser(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
