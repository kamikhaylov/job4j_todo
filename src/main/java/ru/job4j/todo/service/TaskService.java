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
     * @return список задач.
     */
    public List<Task> findAll() {
        return repository.findAll();
    }

    /**
     * Получение списка задач по признаку  выполнения.
     * @return список задач.
     */
    public List<Task> findByDone(boolean done) {
        return repository.findByDone(done);
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
     * @return задача.
     */
    public Optional<Task> update(Task task) {
        return repository.update(task);
    }

    /**
     * Обновить признак выполнения задачи.
     * @return задача.
     */
    public Optional<Task> updateDone(int id) {
        return repository.updateDone(id);
    }

    /**
     * Удалить задачу.
     * @return результат удаления.
     */
    public boolean delete(int id) {
        return repository.delete(id);
    }
}
