package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;

/**
 * Интерфейс взаимодействия с БД заданий
 */
public interface TaskRepository {

    /**
     * Список задач отсортированных по id.
     * @return список пользователей.
     */
    List<Task> findAll();
}
