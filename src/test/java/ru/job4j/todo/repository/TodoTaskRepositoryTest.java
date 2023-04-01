package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.Main;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class TodoTaskRepositoryTest {
    private static SessionFactory sf;

    @BeforeAll
    public static void before() {
        sf = new Main().sf();
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
        TodoTaskRepository store = new TodoTaskRepository(sf);
        Task task1 = new Task(1, "description1", LocalDateTime.now(), false);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), false);

        Task addResult1 = store.add(task1);
        Task addResult2 = store.add(task2);
        List<Task> users = store.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertNotNull(addResult2);
        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(addResult1.getId(), task1.getId());
        Assertions.assertEquals(addResult1.getDescription(), task1.getDescription());
        Assertions.assertEquals(addResult1.getCreated(), task1.getCreated());
        Assertions.assertEquals(addResult1.getDone(), task1.getDone());
        Assertions.assertEquals(addResult2.getId(), task2.getId());
        Assertions.assertEquals(addResult2.getDescription(), task2.getDescription());
        Assertions.assertEquals(addResult2.getCreated(), task2.getCreated());
        Assertions.assertEquals(addResult2.getDone(), task2.getDone());
    }

    @Test
    public void whenUpdate() {
        TodoTaskRepository store = new TodoTaskRepository(sf);
        Task task1 = new Task(1, "description4", LocalDateTime.now(), false);
        Task task2 = new Task(1, "description5", LocalDateTime.now(), false);

        Task addResult1 =  store.add(task1);
        task2.setId(addResult1.getId());
        store.update(task2);
        List<Task> users = store.findAll();
        Optional<Task> result = store.findById(addResult1.getId());

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.get());
        Assertions.assertEquals(result.get().getId(), task2.getId());
        Assertions.assertEquals(result.get().getDescription(), task2.getDescription());
        Assertions.assertEquals(result.get().getCreated(), task2.getCreated());
        Assertions.assertEquals(result.get().getDone(), task2.getDone());
    }

    @Test
    public void whenDelete() {
        TodoTaskRepository store = new TodoTaskRepository(sf);
        Task task1 = new Task(1, "description1", LocalDateTime.now(), false);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), false);

        Task addResult1 = store.add(task1);
        store.add(task2);
        store.delete(addResult1.getId());
        List<Task> users = store.findAll();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(users.get(0).getCreated(), task2.getCreated());
        Assertions.assertEquals(users.get(0).getDone(), task2.getDone());
    }

    @Test
    public void whenFindNew() {
        TodoTaskRepository store = new TodoTaskRepository(sf);
        Task task1 = new Task(1, "description1", LocalDateTime.now(), true);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), false);

        store.add(task1);
        store.add(task2);
        List<Task> users = store.findNew();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(users.get(0).getCreated(), task2.getCreated());
        Assertions.assertEquals(users.get(0).getDone(), task2.getDone());
    }

    @Test
    public void whenFindCompleted() {
        TodoTaskRepository store = new TodoTaskRepository(sf);
        Task task1 = new Task(1, "description1", LocalDateTime.now(), false);
        Task task2 = new Task(2, "description2", LocalDateTime.now(), true);

        store.add(task1);
        store.add(task2);
        List<Task> users = store.findCompleted();

        Assertions.assertNotNull(users);
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(users.get(0).getId(), task2.getId());
        Assertions.assertEquals(users.get(0).getDescription(), task2.getDescription());
        Assertions.assertEquals(users.get(0).getCreated(), task2.getCreated());
        Assertions.assertEquals(users.get(0).getDone(), task2.getDone());
    }
}