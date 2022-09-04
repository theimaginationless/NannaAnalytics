package com.theimless.nannaanalytics.user.service;

import com.theimless.nannaanalytics.common.user.model.Role;
import com.theimless.nannaanalytics.common.user.model.User;
import com.theimless.nannaanalytics.common.user.repository.RoleRepository;
import com.theimless.nannaanalytics.common.user.repository.UserRepository;
import com.theimless.nannaanalytics.common.exception.rest.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class UserService {

    private BCryptPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Autowired
    public UserService(BCryptPasswordEncoder passwordEncoder,
                       UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User getUserByName(String name) throws UsernameNotFoundException {
        var user = userRepository.findByName(name.toLowerCase());

        if (user.isEmpty()) {
            log.info("User '{}' not found", name);
            return null;
        }

        return user.get();
    }

    public User getUserByEmail(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email.toLowerCase());

        if (user.isEmpty()) {
            log.info("User with email '{}' not found", email);
            return null;
        }

        return user.get();
    }

    public User getUserByNameOrEmail(String name, String email) throws UsernameNotFoundException {
        var user = userRepository.findByNameOrEmail(name.toLowerCase(), email.toLowerCase());

        if (user.isEmpty()) {
            log.info("User with name '{}' or email '{}' not found", name, email);
            return null;
        }

        return user.get();
    }

    public User updateRoles(String username, Set<Role> add, Set<Role> remove) {
        var user = getUserByName(username);
        if (user == null) {
            log.error("User '{}' not found", username);
            throw new BadRequestException(String.format("User '%s' not found", username));
        }

        var actualRoles = user.getRoles();
        actualRoles.removeAll(remove);
        actualRoles.addAll(add);
        saveUser(user);
        log.debug("Update roles for user='{}' successful!\nAdded='{}'; Removed='{}'",
                username, add, remove
        );
        return user;
    }

    public User saveUser(User user) {
        user = transformToLowerCase(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            log.debug("User 'id={}' was deleted", id);
            return true;
        }

        return false;
    }

    public boolean isUserExist(String name) {
        return getUserByName(name) != null;
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    private User transformToLowerCase(User user) {
        if (user == null) {
            return null;
        }

        user.setName(user.getName().toLowerCase());
        user.setEmail(user.getEmail().toLowerCase());

        return user;
    }
}
