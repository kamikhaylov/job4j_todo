package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс взаимодействия с БД заданий
 */
public interface TaskRepository {

    /**
     * Получение списка задач.
     * @return список задач.
     */
    List<Task> findAll();

    /**
     * Получение списка новых задач.
     * @return список задач.
     */
    List<Task> findNew();

    /**
     * Получение списка выполненных задач.
     * @return список задач.
     */
    List<Task> findCompleted();

    /**
     * Получение задачи по ID.
     * @return задача.
     */
    Optional<Task> findById(int id);

    /**
     * Создать задачу.
     * @return задача.
     */
    Task add(Task task);

    /**
     * Обновить задачу.
     */
    void update(Task task);

    /**
     * Удалить задачу.
     */
    void delete(int id);
}
