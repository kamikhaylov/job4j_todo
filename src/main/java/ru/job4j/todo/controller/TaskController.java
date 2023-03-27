package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.todo.common.UserSession;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
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
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу со списком всех заданий
     */
    @GetMapping("/tasks")
    public String tasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска всех задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findAll();
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска всех задач: " + tasks);
        return "tasks";
    }

    /**
     * Страница со списком новых заданий
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу со списком новых заданий
     */
    @GetMapping("/newTasks")
    public String newTasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска новых задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findNews();
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска новых задач: " + tasks);
        return "newTasks";
    }

    /**
     * Страница со списком выполненных заданий
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу со списком всех заданий
     */
    @GetMapping("/doneTasks")
    public String doneTasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска выполненных задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findCompleted();
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска выполненных задач: " + tasks);
        return "doneTasks";
    }

    /**
     * Страница добавления новой задачи
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу добавления новой задачи
     */
    @GetMapping("/createTask")
    public String createTask(Model model, HttpSession httpSession) {
        LOGGER.info("Сооздание новой задачи");

        model.addAttribute("user", UserSession.getUser(model, httpSession));
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
        task.setDone(false);
        Task result = service.add(task);

        LOGGER.info("Добавленная задача: " + result);
        return "redirect:/tasks";
    }

    /**
     * Детальная информация по задаче
     * @param model - модель данных
     * @param id - идентификатор задачи
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу с детальной информации по задаче
     */
    @GetMapping("/task/{taskId}")
    public String task(Model model, @PathVariable("taskId") int id, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса получения подробной информации по задаче");

        User user = UserSession.getUser(model, httpSession);
        Task task = service.findById(id).get();
        model.addAttribute("task", task);
        model.addAttribute("user", user);

        LOGGER.info("Сервис получения подробной информации по задаче выполнен: " + task);
        return "task";
    }

    /**
     * Устанавливает признак выполнения зачачи
     * @param id - идентификатор задачи
     * @return возвращает страницу со списком всех задач
     */
    @GetMapping("/done/{taskId}")
    public String done(@PathVariable("taskId") int id) {
        LOGGER.info("Вызов сервиса выполнения задачи");
        Task task = service.findById(id).get();
        task.setDone(true);
        service.update(task);
        return "redirect:/tasks";
    }

    /**
     * Редактирование задачи
     * @param model - модель данных
     * @param id - идентификатор задачи
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу редактирования задачи
     */
    @GetMapping("/task/{taskId}/update")
    public String formUpdateTask(Model model, @PathVariable("taskId") int id, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса обновления информации по задаче");

        User user = UserSession.getUser(model, httpSession);
        model.addAttribute("task", service.findById(id).get());
        model.addAttribute("user", user);
        return "updateTask";
    }

    /**
     * Устанавливает признак выполнения задачи
     * @param task - задача
     * @return возвращает страницу со списком всех задач
     */
    @PostMapping("/update")
    public String update(@ModelAttribute Task task) {
        LOGGER.info("Сохранение отредактированной задачи: " + task);

        service.update(task);

        LOGGER.info("Сервис обновления информации по задаче выполнен");
        return "redirect:/tasks";
    }

    /**
     * Удалениие задачи
     * @param id - идентификатор задачи
     * @return возвращает страницу со списком всех задач
     */
    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") int id) {
        LOGGER.info("Запущен сервис удаления задачи с id: " + id);

        service.delete(id);

        LOGGER.info("Сервис удаления задачи выполнен");
        return "redirect:/tasks";
    }
}
