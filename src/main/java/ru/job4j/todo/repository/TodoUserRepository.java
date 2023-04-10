package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Collections;
import java.util.List;
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

    private final SessionFactory sf;

    /**
     * Добавлениие пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        LOGGER.info("Добавление пользователя: " + user);

        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            result = Optional.of(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        LOGGER.info("Задача в БД добавлена: " + user);
        return result;
    }

    /**
     * Поиск пользователя по логину
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> findUser(User user) {
        LOGGER.info("Поиск пользователя: " + user);

        Session session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            session.beginTransaction();
            result = session
                .createQuery(
                        "FROM User u "
                                + "WHERE u.login = :fLogin "
                                + "AND u.password = :fPassword",
                        User.class)
                .setParameter("fLogin", user.getLogin())
                .setParameter("fPassword", user.getPassword())
                .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        LOGGER.info("Поиск пользователя завершен: " + result);
        return result;
    }

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    public List<User> findAll() {
        LOGGER.info("Поиск всех пользователей");

        Session session = sf.openSession();
        List<User> users = Collections.emptyList();
        try {
            session.beginTransaction();
            users = session
                    .createQuery("FROM User", User.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        LOGGER.info("Поиск всех пользователей в БД завершен, найденные пользователи: " + users);
        return users;
    }
}
