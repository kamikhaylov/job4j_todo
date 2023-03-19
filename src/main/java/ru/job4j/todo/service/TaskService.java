package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

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
     * Получение списка задач.
     * @return список пользователей.
     */
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * Получение задачи по ID.
     * @return задача.
     */
    public Optional<Task> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Добавление новой задачи.
     * @return добавленая задача.
     */
    public Task add(Task task) {
        return repository.add(task);
    }

    /**
     * Обновить задачу.
     */
    public void update(Task task) {
        repository.update(task);
    }

    /**
     * Удалить задачу.
     */
    public void delete(int id) {
        repository.delete(id);
    }
}
