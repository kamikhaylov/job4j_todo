package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.common.TaskModifier;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CategoryRepository;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис задач
 */
@ThreadSafe
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final PriorityRepository priorityRepository;
    private final CategoryRepository categoryRepository;
    private final TaskModifier converterResponse;

    public TaskService(TaskRepository taskRepository,
                       PriorityRepository priorityRepository,
                       CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.priorityRepository = priorityRepository;
        this.categoryRepository = categoryRepository;
        this.converterResponse = new TaskModifier();
    }

    /**
     * Получение списка задач.
     * @return список задач.
     */
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    /**
     * Получение списка задач.
     * @return список задач.
     */
    public List<Task> findAll(String timezone) {
        List<Task> result = taskRepository.findAll();
        return result.stream()
                .map(task -> converterResponse.modify(task, timezone))
                .collect(Collectors.toList());
    }

    /**
     * Получение списка задач по признаку  выполнения.
     * @return список задач.
     */
    public List<Task> findByDone(boolean done) {
        return taskRepository.findByDone(done);
    }

    /**
     * Получение задачи по ID.
     * @return задача.
     */
    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    /**
     * Добавление новой задачи.
     * @param task задача.
     * @param categoryIds список категорий.
     * @return добавленая задача.
     */
    public Task add(Task task, List<Integer> categoryIds) {
        task.setCategories(getCategoriesById(categoryIds));
        return taskRepository.add(task);
    }

    /**
     * Обновить задачу.
     * @return задача.
     */
    public Optional<Task> update(Task task) {
        return taskRepository.update(task);
    }

    /**
     * Обновить признак выполнения задачи.
     * @return задача.
     */
    public boolean updateDone(int id) {
        return taskRepository.updateDone(id);
    }

    /**
     * Удалить задачу.
     * @return результат удаления.
     */
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    /**
     * Получить приоритеты.
     * @return приоритеты.
     */
    public List<Priority> getPriorities() {
        return priorityRepository.getPriorities();
    }

    /**
     * Получение категорий.
     * @return категории.
     */
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Получение категории по списку ID.
     * @return категории.
     */
    public List<Category> getCategoriesById(List<Integer> ids) {
        return categoryRepository.findByIds(ids);
    }

}
