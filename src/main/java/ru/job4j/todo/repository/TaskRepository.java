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
     * Получение задачи по ID.
     * @return задача.
     */
    Optional<Task> findById(int id);

    /**
     * Создать задачу.
     * @param task задача.
     * @return задача.
     */
    Task add(Task task);

    /**
     * Обновить задачу.
     * @param task задача.
     * @return задача.
     */
    Optional<Task> update(Task task);

    /**
     * Обновляет признак у задачи.
     * @param id идентификатор задачи.
     * @return задача.
     */
    Optional<Task> updateDone(int id);

    /**
     * Удалить задачу.
     * @param id идентификатор задания.
     * @return результат удаления.
     */
    boolean delete(int id);

    /**
     * Получение списка выполненных задач из БД.
     * @param done признак выполнения задачи.
     * @return список задач по фильтру done.
     */
    List<Task> findByDone(boolean done);
}
