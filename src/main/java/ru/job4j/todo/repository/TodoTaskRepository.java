package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;

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
     * Список задач.
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
     * Создать задачу.
     * @return список пользователей.
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
}