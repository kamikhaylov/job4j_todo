package ru.job4j.todo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskControllerTest {
    private static final int ID_TEST = 1;
    private static final User USER = new User(1, "User", "login", "pass", null);
    private static final Priority PRIORITY = new Priority(1, "Средний", 2);
    private static final List<Category> CATEGORIES = List.of(new Category(1, "Разработка"));
    private static final List<Integer> CATEGORY_IDS = List.of(1);

    @Mock
    private Model model;
    @Mock
    private TaskService service;
    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, service, httpSession);
    }

    @Test
    public void whenTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task_01", LocalDateTime.now(), false, USER, PRIORITY, CATEGORIES),
                new Task(2, "Task_02", LocalDateTime.now(), true, USER, PRIORITY, CATEGORIES)
        );
        when(service.findAll(null)).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.tasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "tasks/tasks");
    }

    @Test
    public void whenNewTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task_01", LocalDateTime.now(), false, USER, PRIORITY, CATEGORIES),
                new Task(2, "Task_02", LocalDateTime.now(), false, USER, PRIORITY, CATEGORIES)
        );
        when(service.findByDone(false)).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.newTasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "tasks/new");
    }

    @Test
    public void whenDoneTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task_01", LocalDateTime.now(), true, USER, PRIORITY, CATEGORIES),
                new Task(2, "Task_02", LocalDateTime.now(), true, USER, PRIORITY, CATEGORIES)
        );
        when(service.findByDone(true)).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.doneTasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "tasks/done");
    }

    @Test
    public void whenCreateTask() {
        TaskController controller = new TaskController(service);
        String page = controller.createTask(model, httpSession);

        Assertions.assertEquals(page, "tasks/create");
    }

    @Test
    public void whenAddTask() {
        Task task = new Task(1, "Task_01", LocalDateTime.now(), false, USER, PRIORITY, CATEGORIES);
        when(service.add(task, CATEGORY_IDS)).thenReturn(task);
        TaskController controller = new TaskController(service);
        String page = controller.addTask(task, USER, CATEGORY_IDS);

        verify(service).add(task, CATEGORY_IDS);
        Assertions.assertEquals(page, "redirect:/tasks/all");
    }

    @Test
    public void whenTask() {
        Task task = new Task(ID_TEST, "Task_01", LocalDateTime.now(), false, USER, PRIORITY,
                CATEGORIES);
        when(service.findById(ID_TEST)).thenReturn(Optional.of(task));
        TaskController controller = new TaskController(service);
        String page = controller.task(model, ID_TEST, httpSession);

        verify(model).addAttribute("task", task);
        verify(service).findById(ID_TEST);
        Assertions.assertEquals(page, "tasks/task");
    }

    @Test
    public void whenDone() {
        TaskController controller = new TaskController(service);
        Task task = new Task(1, "Task_01", LocalDateTime.now(), true, USER, PRIORITY,
                CATEGORIES);
        when(service.updateDone(task.getId())).thenReturn(true);

        String page = controller.done(ID_TEST);

        verify(service).updateDone(ID_TEST);
        Assertions.assertEquals(page, "redirect:/tasks/all");
    }

    @Test
    public void whenDoneFail() {
        TaskController controller = new TaskController(service);
        Task task = new Task(1, "Task_01", LocalDateTime.now(), true, USER, PRIORITY, CATEGORIES);
        when(service.updateDone(task.getId())).thenReturn(false);

        String page = controller.done(ID_TEST);

        verify(service).updateDone(ID_TEST);
        Assertions.assertEquals(page, "redirect:/tasks/error?fail=update");
    }

    @Test
    public void whenFormUpdateTask() {
        Task task = new Task(ID_TEST, "Task_01", LocalDateTime.now(), true, USER, PRIORITY,
                CATEGORIES);
        when(service.findById(ID_TEST)).thenReturn(Optional.of(task));
        TaskController controller = new TaskController(service);
        String page = controller.formUpdateTask(model, ID_TEST, httpSession);

        verify(model).addAttribute("task", task);
        Assertions.assertEquals(page, "tasks/update");
    }

    @Test
    public void whenUpdate() {
        Task task = new Task(1, "Task_01", LocalDateTime.now(), true, USER, PRIORITY,
                CATEGORIES);
        TaskController controller = new TaskController(service);
        when(service.update(task)).thenReturn(Optional.of(task));

        String page = controller.update(model, task, httpSession);

        verify(service).update(task);
        Assertions.assertEquals(page, "redirect:/tasks/all");
    }

    @Test
    public void whenUpdateFail() {
        Task task = new Task(1, "Task_01", LocalDateTime.now(), true, USER, PRIORITY, CATEGORIES);
        TaskController controller = new TaskController(service);
        when(service.update(task)).thenReturn(Optional.empty());

        String page = controller.update(model, task, httpSession);

        verify(service).update(task);
        Assertions.assertEquals(page, "redirect:/tasks/error?fail=update");
    }

    @Test
    public void whenDelete() {
        TaskController controller = new TaskController(service);
        when(service.delete(ID_TEST)).thenReturn(true);
        String page = controller.deleteTask(ID_TEST);

        verify(service).delete(ID_TEST);
        Assertions.assertEquals(page, "redirect:/tasks/all");
    }

    @Test
    public void whenDeleteFail() {
        TaskController controller = new TaskController(service);
        when(service.delete(ID_TEST)).thenReturn(false);
        String page = controller.deleteTask(ID_TEST);

        verify(service).delete(ID_TEST);
        Assertions.assertEquals(page, "redirect:/tasks/error?fail=delete");
    }
}