package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер задач
 */
@ThreadSafe
@Controller
public class TaskController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class.getName());

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    /**
     * Страница со списком всех заданий
     * @param model - модель данных
     * @return возвращает страницу со списком всех заданий
     */
    @GetMapping("/tasks")
    public String tasks(Model model) {
        LOGGER.info("Вызов сервиса поиска всех задач");

        List<Task> tasks = service.findAllOrderById();
        model.addAttribute("tasks", tasks);

        LOGGER.info("Результат вызова сервиса посисках всех задач: " + tasks);
        return "tasks";
    }

    /**
     * Страница добавления новой задачи
     * @return возвращает страницу добавления новой задачи
     */
    @GetMapping("/createTask")
    public String createTask() {
        LOGGER.info("Вызов сервиса поиска всех задач");

        return "createTask";
    }

    /**
     * Добавление новой задачи
     * @param task - задача
     * @return возвращает страницу со списком всех заданий
     */
    @PostMapping("/addTask")
    public String addTask(@ModelAttribute Task task) {
        LOGGER.info("Вызов сервиса добавления новой задачи");

        task.setCreated(LocalDateTime.now());
        Task result = service.add(task);

        LOGGER.info("Добавленная задача: " + result);
        return "redirect:/tasks";
    }
}
