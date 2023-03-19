package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Взаимодействие с БД заданий
 */
@Repository
@AllArgsConstructor
@ThreadSafe
public class TodoTaskRepository implements TaskRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TodoTaskRepository.class.getName());

    private final SessionFactory sf;

    /**
     * Получение списка задач из БД.
     * @return список пользователей.
     */
    @Override
    public List<Task> findAll() {
        LOGGER.info("Запущен поиск всех задач в БД");

        Session session = sf.openSession();
        List<Task> tasks = session
                .createQuery("FROM Task", Task.class)
                .list();
        session.close();

        LOGGER.info("Поиск всех задач в БД завершен, найденные задачи: " + tasks);
        return tasks;
    }

    /**
     * Получение задачи по ID.
     * @return задача.
     */
    @Override
    public Optional<Task> findById(int id) {
        LOGGER.info("Запущен поиск задачи по Id");

        Session session = sf.openSession();
        Optional<Task> task = session
                .createQuery("FROM Task t WHERE t.id = :fId", Task.class)
                .setParameter("fId", id)
                .uniqueResultOptional();
        session.close();

        LOGGER.info("Поиск задачи по Id в БД завершен, найденная задача: " + Optional.of(task));
        return task;
    }

    /**
     * Создать задачу.
     * @return задача.
     */
    @Override
    public Task add(Task task) {
        LOGGER.info("Добавление новой задачи в БД");

        Session session = sf.openSession();
        session.beginTransaction();
        session.save(task);
        session.getTransaction().commit();
        session.close();

        LOGGER.info("Задача в БД добавлена: " + task);
        return task;
    }

    /**
     * Обновить задачу.
     */
    @Override
    public void update(Task task) {
        LOGGER.info("Обновление задачи в БД: " + task);

        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("UPDATE Task SET description = :fDescription, created = :fCreated, done = :fDone "
                    + "WHERE id = :fId")
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.getDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить задачу.
     */
    @Override
    public void delete(int id) {
        LOGGER.info("Удаление задачи в БД: " + id);

        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            session.getTransaction().rollback();
        }
    }
}