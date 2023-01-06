package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;

/**
 * Сервис задач
 */
@ThreadSafe
@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Список задач отсортированных по id.
     * @return список пользователей.
     */
    public List<Task> findAllOrderById() {
        return repository.findAll();
    }

    /**
     * Добавление новой задачи.
     * @return добавленая задача.
     */
    public Task add(Task task) {
        return repository.add(task);
    }
}
