package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.config.HibernateConfiguration;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

class TodoTaskRepositoryTest {
    private static final User USER = new User(1, "User", "login", "pass", null);
    private static final Priority PRIORITY = new Priority(1, "Средний", 2);
    private static final List<Category> CATEGORIES = List.of(new Category(1, "Разработка"));

    private static SessionFactory sf;
    private static CrudRepository crudRepository;
    private static UserRepository userStore;
    private static PriorityRepository priorityRepository;
    private static CategoryRepository categoryRepository;
    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @BeforeAll
    public static void before() {
        sf = new HibernateConfiguration().sf();
        HibernateConfiguration hc = new HibernateConfiguration();
        sf = hc.sf();
        crudRepository = new CrudRepositoryImpl(sf);
        userStore = new TodoUserRepository(crudRepository);
        priorityRepository = new TodoPriorityRepository(crudRepository);
        categoryRepository = new TodoCategoryRepository(crudRepository);
        priorityRepository.add(PRIORITY);
        categoryRepository.add(CATEGORIES.get(0));
        userStore.add(USER);
    }

    @AfterAll
    public static void after() {
        sf.close();
    }

    @AfterEach
    public void clearDB() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("DELETE Task").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenAdd() {
        TaskRepository taskRepository = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();

        Task task1 = new Task(1, "description1", LocalDateTime.now(),
                false, userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);
        Task task2 = new Task(2, "description2", LocalDateTime.now(),
                false, userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        Task addResult1 = taskRepository.add(task1);
        Task addResult2 = taskRepository.add(task2);
        List<Task> tasks = taskRepository.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertNotNull(addResult2);
        Assertions.assertEquals(tasks.size(), 2);
        Assertions.assertEquals(addResult1.getId(), task1.getId());
        Assertions.assertEquals(addResult1.getDescription(), task1.getDescription());
        Assertions.assertEquals(
                addResult1.getCreated().format(format), task1.getCreated().format(format));
        Assertions.assertEquals(addResult1.isDone(), task1.isDone());
        Assertions.assertEquals(addResult2.getId(), task2.getId());
        Assertions.assertEquals(addResult2.getDescription(), task2.getDescription());
        Assertions.assertEquals(
                addResult2.getCreated().format(format), task2.getCreated().format(format));
        Assertions.assertEquals(addResult2.isDone(), task2.isDone());
    }

    @Test
    public void whenUpdate() {
        TaskRepository taskRepository = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();

        Task task1 = new Task(1, "description4", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);
        Task task2 = new Task(1, "description5", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        Task addResult1 =  taskRepository.add(task1);
        task2.setId(addResult1.getId());
        taskRepository.update(task2);
        List<Task> tasks = taskRepository.findAll();
        Optional<Task> result = taskRepository.findById(addResult1.getId());

        Assertions.assertNotNull(tasks);
        Assertions.assertEquals(tasks.size(), 1);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(result.get().getId(), task2.getId());
        Assertions.assertEquals(result.get().getDescription(), task2.getDescription());
        Assertions.assertEquals(
                result.get().getCreated().format(format), task2.getCreated().format(format));
        Assertions.assertEquals(result.get().isDone(), task2.isDone());
    }

    @Test
    public void whenUpdateDone() {
        TaskRepository store = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();
        Task task = new Task(1, "description8", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        Task addResult = store.add(task);
        store.updateDone(addResult.getId());
        Optional<Task> result = store.findById(addResult.getId());

        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(result.get().getDescription(), task.getDescription());
        Assertions.assertEquals(
                result.get().getCreated().format(format), task.getCreated().format(format));
        Assertions.assertTrue(result.get().isDone());
    }

    @Test
    public void whenDelete() {
        TaskRepository store = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();
        Task task1 = new Task(1, "description1", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        Task addResult1 = store.add(task1);
        store.add(task2);
        store.delete(addResult1.getId());
        List<Task> users = store.findAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(
                users.get(0).getCreated().format(format), task2.getCreated().format(format));
        Assertions.assertEquals(users.get(0).isDone(), task2.isDone());
    }

    @Test
    public void whenFindNew() {
        TaskRepository store = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();
        Task task1 = new Task(1, "description1", LocalDateTime.now(), true,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        store.add(task1);
        store.add(task2);
        List<Task> users = store.findByDone(false);

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(
                users.get(0).getCreated().format(format), task2.getCreated().format(format));
        Assertions.assertEquals(users.get(0).isDone(), task2.isDone());
    }

    @Test
    public void whenFindCompleted() {
        TaskRepository store = new TodoTaskRepository(crudRepository);
        List<Priority> priorities = priorityRepository.getPriorities();
        Task task1 = new Task(1, "description1", LocalDateTime.now(), false,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), true,
                userStore.findUser(USER).get(), priorities.get(0), CATEGORIES);

        store.add(task1);
        store.add(task2);
        List<Task> users = store.findByDone(true);

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(
                users.get(0).getCreated().format(format), task2.getCreated().format(format));
        Assertions.assertEquals(users.get(0).isDone(), task2.isDone());
    }
}