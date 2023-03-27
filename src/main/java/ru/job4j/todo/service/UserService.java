package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserRepository;

import java.util.Optional;

/**
 * Сервис пользователей
 */
@ThreadSafe
@Service
public class UserService {
    private final UserRepository store;

    public UserService(UserRepository store) {
        this.store = store;
    }

    /**
     * Добавление нового пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        return store.add(user);
    }

    /**
     * Поиск пользователя по логину
     * @param login - логин
     * @return возвращает пользователя
     */
    public Optional<User> findUserByLogin(String login) {
        return store.findUserByLogin(login);
    }
}
