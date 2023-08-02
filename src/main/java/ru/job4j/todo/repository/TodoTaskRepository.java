package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final CrudRepository crudRepository;

    /**
     * Получение списка задач из БД.
     * @return список задач.
     */
    @Override
    public List<Task> findAll() {
        LOGGER.info("Запущен поиск всех задач в БД");
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = crudRepository.query("FROM Task t JOIN FETCH t.priority", Task.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
        try {
            task = crudRepository.optional(
                    "FROM Task t JOIN FETCH t.priority "
                            + "WHERE t.id = :fId",
                    Task.class,
                    Map.of("fId", id)
            );
            LOGGER.info("Поиск задачи по Id в БД завершен, найденная задача: " + Optional.of(task));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
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
        try {
            crudRepository.run(session -> session.save(task));
            LOGGER.info("Задача в БД добавлена: " + task);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
        try {
            crudRepository.run(session -> session.merge(task));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
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
        boolean result = false;
        try {
            crudRepository.run(
                    "UPDATE Task SET done = 'true' WHERE id = :fId",
                    Map.of("fId", id)
            );
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Удалить задачу.
     * @param id идентификатор задания.
     */
    @Override
    public boolean delete(int id) {
        LOGGER.info("Удаление задачи в БД: " + id);
        boolean result = false;
        try {
            crudRepository.run(
                    "DELETE Task WHERE id = :fId",
                    Map.of("fId", id)
            );
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Получение списка выполненных задач из БД.
     * @param done признак выполнения задачи.
     * @return список задач по фильтру done.
     */
    public List<Task> findByDone(boolean done) {
        LOGGER.info("Запущен поиск выполненных задач в БД");
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = crudRepository.query(
                    "FROM Task t JOIN FETCH t.priority"
                            + " WHERE t.done = :fDone", Task.class,
                    Map.of("fDone", done)
            );
            LOGGER.info("Поиск задач в БД завершен, найденные задачи: " + tasks);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return tasks;
    }
}