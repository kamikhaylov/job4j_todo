package ru.job4j.todo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Task;
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
                new Task(1, "Task_01", LocalDateTime.now(), false),
                new Task(2, "Task_02", LocalDateTime.now(), true)
        );
        when(service.findAll()).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.tasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "tasks");
    }

    @Test
    public void whenNewTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task_01", LocalDateTime.now(), false),
                new Task(2, "Task_02", LocalDateTime.now(), false)
        );
        when(service.findNews()).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.newTasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "newTasks");
    }

    @Test
    public void whenDoneTasks() {
        List<Task> tasks = Arrays.asList(
                new Task(1, "Task_01", LocalDateTime.now(), true),
                new Task(2, "Task_02", LocalDateTime.now(), true)
        );
        when(service.findCompleted()).thenReturn(tasks);
        TaskController controller = new TaskController(service);
        String page = controller.doneTasks(model, httpSession);

        verify(model).addAttribute("tasks", tasks);
        Assertions.assertEquals(page, "doneTasks");
    }

    @Test
    public void whenCreateTask() {
        TaskController controller = new TaskController(service);
        String page = controller.createTask(model, httpSession);

        Assertions.assertEquals(page, "createTask");
    }

    @Test
    public void whenAddTask() {
        Task task = new Task(1, "Task_01", LocalDateTime.now(), false);
        when(service.add(task)).thenReturn(task);
        TaskController controller = new TaskController(service);
        String page = controller.addTask(task);

        verify(service).add(task);
        Assertions.assertEquals(page, "redirect:/tasks");
    }

    @Test
    public void whenTask() {
        Task task = new Task(ID_TEST, "Task_01", LocalDateTime.now(), false);
        when(service.findById(ID_TEST)).thenReturn(Optional.of(task));
        TaskController controller = new TaskController(service);
        String page = controller.task(model, ID_TEST, httpSession);

        verify(model).addAttribute("task", task);
        verify(service).findById(ID_TEST);
        Assertions.assertEquals(page, "task");
    }

    @Test
    public void whenDone() {
        Task task = new Task(ID_TEST, "Task_01", LocalDateTime.now(), true);
        when(service.findById(ID_TEST)).thenReturn(Optional.of(task));
        TaskController controller = new TaskController(service);
        String page = controller.done(ID_TEST);

        verify(service).update(task);
        Assertions.assertEquals(page, "redirect:/tasks");
    }

    @Test
    public void whenFormUpdateTask() {
        Task task = new Task(ID_TEST, "Task_01", LocalDateTime.now(), true);
        when(service.findById(ID_TEST)).thenReturn(Optional.of(task));
        TaskController controller = new TaskController(service);
        String page = controller.formUpdateTask(model, ID_TEST, httpSession);

        verify(model).addAttribute("task", task);
        Assertions.assertEquals(page, "updateTask");
    }

    @Test
    public void whenUpdate() {
        Task task = new Task(1, "Task_01", LocalDateTime.now(), true);
        TaskController controller = new TaskController(service);
        String page = controller.update(task);

        verify(service).update(task);
        Assertions.assertEquals(page, "redirect:/tasks");
    }

    @Test
    public void whenDelete() {
        TaskController controller = new TaskController(service);
        String page = controller.deleteTask(ID_TEST);

        verify(service).delete(ID_TEST);
        Assertions.assertEquals(page, "redirect:/tasks");
    }
}