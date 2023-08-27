package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Взаимодействие с БД категорий
 */
@Repository
@AllArgsConstructor
@ThreadSafe
public class TodoCategoryRepository implements CategoryRepository {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(TodoCategoryRepository.class.getName());

    private final CrudRepository crudRepository;

    /**
     * Получение категорий.
     * @return категории.
     */
    @Override
    public List<Category> findAll() {
        LOGGER.info("Запущен поиск всех категорий в БД");
        List<Category> categories = new ArrayList<>();
        try {
            categories = crudRepository.query("FROM Category", Category.class);
            LOGGER.info("Завершен поиск всех категорий в БД");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return categories;
    }

    /**
     * Получение категории по списку ID.
     * @return категории.
     */
    @Override
    public List<Category> findByIds(List<Integer> ids) {
        LOGGER.info("Запущен поиск категории по списку id в БД");
        List<Category> categories = new ArrayList<>();
        try {
            categories = crudRepository.query("FROM Category WHERE id IN (:ids)", Category.class,
                    Map.of("ids", ids));
            LOGGER.info("Поиск категории по Id в БД завершена");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return categories;
    }

    /**
     * Создать задачу.
     * @param category задача.
     * @return задача.
     */
    @Override
    public Category add(Category category) {
        LOGGER.info("Добавление новой категории в БД");
        try {
            crudRepository.run(session -> session.save(category));
            LOGGER.info("Завершено добавление в БД новой категории");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return category;
    }

}
