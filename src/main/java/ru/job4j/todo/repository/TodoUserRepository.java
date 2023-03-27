package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

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
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

        LOGGER.info("Задача в БД добавлена: " + user);
        return Optional.of(user);
    }

    /**
     * Поиск пользователя по логину
     * @param login - логин
     * @return возвращает пользователя
     */
    public Optional<User> findUserByLogin(String login) {
        LOGGER.info("Поиск пользователя по логину: " + login);

        Session session = sf.openSession();
        Optional<User> user = session
                .createQuery("FROM User u WHERE u.login = :fLogin", User.class)
                .setParameter("fLogin", login)
                .uniqueResultOptional();
        session.close();

        LOGGER.info("Поиск пользователя по логину завершен: " + user);
        return user;
    }

    /**
     * Поиск всех пользователей
     * @return возвращает список пользователей
     */
    public List<User> findAll() {
        LOGGER.info("Поиск всех пользователей");

        Session session = sf.openSession();
        List<User> users = session
                .createQuery("FROM User", User.class)
                .list();
        session.close();

        LOGGER.info("Поиск всех пользователей в БД завершен, найденные пользователи: " + users);
        return users;
    }
}
