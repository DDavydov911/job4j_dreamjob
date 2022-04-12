package ru.job4j.dream.service;

import org.springframework.stereotype.Service;
import ru.job4j.dream.model.User;
import ru.job4j.dream.store.UserStoreDb;

import java.util.Optional;

@Service
public class UserService {

    private final UserStoreDb userStoreDb;

    public UserService(UserStoreDb userStoreDb) {
        this.userStoreDb = userStoreDb;
    }

    public Optional<User> add(User user) {
        return userStoreDb.add(user);
    }

    public Optional<User> findById(int id) {
        return userStoreDb.findById(id);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        return userStoreDb.findUserByEmailAndPwd(email, password);
    }
}
