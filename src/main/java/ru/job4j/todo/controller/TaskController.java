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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.common.UserSession;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер задач
 */
@ThreadSafe
@Controller
@RequestMapping("/tasks")
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
    @GetMapping("/all")
    public String tasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска всех задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findAll();
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска всех задач: " + tasks);
        return "tasks/tasks";
    }

    /**
     * Страница со списком новых заданий
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу со списком новых заданий
     */
    @GetMapping("/new")
    public String newTasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска новых задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findByDone(false);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска новых задач: " + tasks);
        return "tasks/new";
    }

    /**
     * Страница со списком выполненных заданий
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу со списком всех заданий
     */
    @GetMapping("/done")
    public String doneTasks(Model model, HttpSession httpSession) {
        LOGGER.info("Вызов сервиса поиска выполненных задач");

        User user = UserSession.getUser(model, httpSession);
        List<Task> tasks = service.findByDone(true);
        model.addAttribute("tasks", tasks);
        model.addAttribute("user", user);

        LOGGER.info("Результат вызова сервиса поиска выполненных задач: " + tasks);
        return "tasks/done";
    }

    /**
     * Страница добавления новой задачи
     * @param model - модель данных
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу добавления новой задачи
     */
    @GetMapping("/create")
    public String createTask(Model model, HttpSession httpSession) {
        LOGGER.info("Создание новой задачи");

        model.addAttribute("user", UserSession.getUser(model, httpSession));
        return "tasks/create";
    }

    /**
     * Добавление новой задачи
     * @param task - задача
     * @return возвращает страницу со списком всех заданий
     */
    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        LOGGER.info("Вызов сервиса добавления новой задачи");

        task.setCreated(LocalDateTime.now());
        task.setDone(false);
        Task result = service.add(task);

        LOGGER.info("Добавленная задача: " + result);
        return "redirect:/tasks/all";
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
        Optional<Task> task = service.findById(id);
        if (task.isEmpty()) {
            return "redirect:/tasks/error?fail=find";
        }
        model.addAttribute("task", task.get());
        model.addAttribute("user", user);

        LOGGER.info("Сервис получения подробной информации по задаче выполнен: " + task);
        return "tasks/task";
    }

    /**
     * Устанавливает признак выполнения зачачи
     * @param id - идентификатор задачи
     * @return возвращает страницу со списком всех задач
     */
    @GetMapping("/done/{taskId}") public String done(@PathVariable("taskId") int id) {
        LOGGER.info("Вызов сервиса выполнения задачи");
        boolean result = service.updateDone(id);
        if (!result) {
            return "redirect:/tasks/error?fail=update";
        }
        return "redirect:/tasks/all";
    }

    /**
     * Редактирование задачи
     * @param model - модель данных
     * @param id - идентификатор задачи
     * @param httpSession - пользовательская сессия
     * @return возвращает страницу редактирования задачи
     */
    @GetMapping("/task/{taskId}/update")
    public String formUpdateTask(Model model, @PathVariable("taskId") int id,
                                 HttpSession httpSession) {
        LOGGER.info("Вызов сервиса обновления информации по задаче");

        User user = UserSession.getUser(model, httpSession);
        Optional<Task> task = service.findById(id);
        if (task.isEmpty()) {
            return "redirect:/tasks/error?fail=find";
        }
        model.addAttribute("task", task.get());
        model.addAttribute("user", user);
        return "tasks/update";
    }

    /**
     * Устанавливает признак выполнения задачи
     * @param task - задача
     * @return возвращает страницу со списком всех задач
     */
    @PostMapping("/update")
    public String update(Model model, @ModelAttribute Task task, HttpSession httpSession) {
        LOGGER.info("Сохранение отредактированной задачи: " + task);
        User user = UserSession.getUser(model, httpSession);
        Optional<Task> result = service.update(task);
        model.addAttribute("user", user);
        if (result.isEmpty()) {
            return "redirect:/tasks/error?fail=update";
        }
        LOGGER.info("Сервис обновления информации по задаче выполнен");
        return "redirect:/tasks/all";
    }

    @GetMapping("/error")
    public String error(Model model,
                        @RequestParam(name = "fail", required = false) String fail,
                        HttpSession httpSession) {
        LOGGER.info("Страница с ошибкой");

        User user = UserSession.getUser(model, httpSession);
        model.addAttribute("fail", fail);
        model.addAttribute("user", user);
        return "tasks/error";
    }

    /**
     * Удалениие задачи
     * @param id - идентификатор задачи
     * @return возвращает страницу со списком всех задач
     */
    @GetMapping("/delete/{taskId}")
    public String deleteTask(@PathVariable("taskId") int id) {
        LOGGER.info("Запущен сервис удаления задачи с id: " + id);

        boolean result = service.delete(id);
        if (!result) {
            return "redirect:/tasks/error?fail=delete";
        }
        LOGGER.info("Сервис удаления задачи выполнен");
        return "redirect:/tasks/all";
    }
}
