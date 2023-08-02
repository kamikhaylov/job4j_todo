package ru.job4j.todo.repository;

import ru.job4j.todo.model.Priority;

import java.util.List;

/**
 * Интерфейс взаимодействия с таблицей приоритетов.
 */
public interface PriorityRepository {

    /**
     * Добавить приоритет.
     * @return приоритет.
     */
    Priority add(Priority priority);

    /**
     * Получить приоритеты.
     * @return приоритеты.
     */
    List<Priority> getPriorities();
}
