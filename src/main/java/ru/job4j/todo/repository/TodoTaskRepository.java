package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
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
     * @return список задач.
     */
    @Override
    public List<Task> findAll() {
        LOGGER.info("Запущен поиск всех задач в БД");

        List<Task> tasks = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            tasks = session
                    .createQuery("FROM Task", Task.class)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }

        return tasks;
    }

    /**
     * Получение задачи по ID.
     * @param id идентификатор задания.
     * @return задача.
     */
    @Override
    public Optional<Task> findById(int id) {
        LOGGER.info("Запущен поиск задачи по Id");

        Optional<Task> task = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            task = session
                    .createQuery("FROM Task t WHERE t.id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            session.close();
        }

        LOGGER.info("Поиск задачи по Id в БД завершен, найденная задача: " + Optional.of(task));
        return task;
    }

    /**
     * Создать задачу.
     * @param task задача.
     * @return задача.
     */
    @Override
    public Task add(Task task) {
        LOGGER.info("Добавление новой задачи в БД");

        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
            LOGGER.info("Задача в БД добавлена: " + task);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return task;
    }

    /**
     * Обновить задачу.
     * @param task задача.
     * @return задача.
     */
    @Override
    public Optional<Task> update(Task task) {
        LOGGER.info("Обновление задачи в БД: " + task);

        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
            return Optional.empty();
        } finally {
            session.close();
        }
        return findById(task.getId());
    }

    /**
     * Обновляет признак у задачи.
     * @param id идентификатор задачи.
     * @return задача.
     */
    @Override
    public boolean updateDone(int id) {
        LOGGER.info("Обновление признака задачи в БД: " + id);
        Session session = sf.openSession();
        int result = 0;
        try {
            session.beginTransaction();
            result = session.createQuery(
                    "UPDATE Task SET done = 'true' WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result > 0;
    }

    /**
     * Удалить задачу.
     * @param id идентификатор задания.
     */
    @Override
    public boolean delete(int id) {
        LOGGER.info("Удаление задачи в БД: " + id);

        Session session = sf.openSession();
        int result = 0;
        try {
            session.beginTransaction();
            result = session.createQuery("DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result > 0;
    }

    /**
     * Получение списка выполненных задач из БД.
     * @param done признак выполнения задачи.
     * @return список задач по фильтру done.
     */
    public List<Task> findByDone(boolean done) {
        LOGGER.info("Запущен поиск выполненных задач в БД");

        List<Task> tasks = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            tasks = session
                    .createQuery("FROM Task t WHERE t.done = :fDone", Task.class)
                    .setParameter("fDone", done)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }

        LOGGER.info("Поиск задач в БД завершен, найденные задачи: " + tasks);
        return tasks;
    }
}