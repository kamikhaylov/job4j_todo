package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * Взаимодействие с БД приоритетов
 */
@Repository
@AllArgsConstructor
@ThreadSafe
public class TodoPriorityRepository implements PriorityRepository {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(TodoPriorityRepository.class.getName());

    private final CrudRepository crudRepository;

    /**
     * Добавить приоритет.
     * @return приоритет.
     */
    public Priority add(Priority priority) {
        LOGGER.info("Добавление нового приоритета в БД");
        try {
            crudRepository.run(session -> session.save(priority));
            LOGGER.info("Приоритет в БД добавлен: " + priority);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return priority;
    }

    /**
     * Получить приоритеты.
     * @return приоритеты.
     */
    public List<Priority> getPriorities() {
        LOGGER.info("Запущен поиск приоритетов в БД");
        List<Priority> priorities = new ArrayList<>();
        try {
            priorities = crudRepository.query(
                    "FROM Priority p", Priority.class
            );
            LOGGER.info("Поиск приоритетов в БД завершен, найденные приоритеты: " + priorities);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return priorities;
    }
}
