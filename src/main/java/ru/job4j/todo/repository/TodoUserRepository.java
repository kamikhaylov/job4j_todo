package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Работа с БД пользователей
 */
@Repository
@AllArgsConstructor
@ThreadSafe
public class TodoUserRepository implements UserRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TodoUserRepository.class.getName());

    private final CrudRepository crudRepository;

    /**
     * Добавлениие пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        LOGGER.info("Добавление пользователя: " + user);
        Optional<User> result = Optional.empty();
        try {
            crudRepository.run(session -> session.save(user));
            result = Optional.of(user);
            LOGGER.info("Пользователь в БД добавлен: " + user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Поиск пользователя по логину
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> findUser(User user) {
        LOGGER.info("Поиск пользователя: " + user);
        Optional<User> result = Optional.empty();
        try {
            result = crudRepository.optional(
                    "FROM User u "
                            + "WHERE u.login = :fLogin "
                            + "AND u.password = :fPassword",
                    User.class,
                    Map.of("fLogin", user.getLogin(),
                            "fPassword", user.getPassword())
            );
            LOGGER.info("Поиск пользователя завершен: " + result);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    public List<User> findAll() {
        LOGGER.info("Поиск всех пользователей");
        List<User> users = Collections.emptyList();
        try {
            users = crudRepository.query("FROM User", User.class);
            LOGGER.info("Поиск всех пользователей в БД завершен, найденные пользователи: " + users);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return users;
    }
}
