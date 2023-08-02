package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис задач
 */
@ThreadSafe
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final PriorityRepository priorityRepository;

    public TaskService(TaskRepository taskRepository, PriorityRepository priorityRepository) {
        this.taskRepository = taskRepository;
        this.priorityRepository = priorityRepository;
    }

    /**
     * Получение списка задач.
     * @return список задач.
     */
    public List<Task> findAll() {
        return taskRepository.findAll();
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
     * @return добавленая задача.
     */
    public Task add(Task task) {
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
}
