package ru.job4j.todo.repository;

import ru.job4j.todo.model.Category;

import java.util.List;

/**
 * Интерфейс взаимодействия с БД заданий
 */
public interface CategoryRepository {

    /**
     * Получение категорий.
     * @return категории.
     */
    List<Category> findAll();

    /**
     * Получение категории по списку ID.
     * @return категории.
     */
    List<Category> findByIds(List<Integer> ids);

    /**
     * Создать задачу.
     * @param category задача.
     * @return задача.
     */
    Category add(Category category);

}
