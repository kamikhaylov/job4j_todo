package ru.job4j.todo.repository;

import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс взаимодействия с БД пользователей
 */
public interface UserRepository {

    /**
     * Добавлениие пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    Optional<User> add(User user);

    /**
     * Поиск пользователя по логину
     * @param user - пользователь
     * @return возвращает пользователя
     */
    Optional<User> findUser(User user);

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    List<User> findAll();
}
