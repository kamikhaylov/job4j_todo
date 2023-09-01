package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.job4j.todo.config.HibernateConfiguration;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

class TodoUserRepositoryTest {
    private static SessionFactory sf;
    private static CrudRepository crudRepository;

    @BeforeAll
    public static void before() {
        sf = new HibernateConfiguration().sf();
        crudRepository = new CrudRepositoryImpl(sf);
    }

    @AfterAll
    public static void after() {
        sf.close();
    }

    @AfterEach
    public void clearDB() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("DELETE User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test()
    public void whenAdd() {
        UserRepository store = new TodoUserRepository(crudRepository);
        User user1 = new User(0, "user1", "login1", "123", null);
        User user2 = new User(1, "user2", "login2", "321", null);

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);
        List<User> users = store.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertNotNull(addResult2);
        Assertions.assertEquals(users.size(), 2);
        Assertions.assertEquals(addResult1.get().getId(), user1.getId());
        Assertions.assertEquals(addResult1.get().getName(), user1.getName());
        Assertions.assertEquals(addResult1.get().getLogin(), user1.getLogin());
        Assertions.assertEquals(addResult1.get().getPassword(), user1.getPassword());
        Assertions.assertEquals(addResult2.get().getId(), user2.getId());
        Assertions.assertEquals(addResult2.get().getName(), user2.getName());
        Assertions.assertEquals(addResult2.get().getLogin(), user2.getLogin());
        Assertions.assertEquals(addResult2.get().getPassword(), user2.getPassword());
    }

    @Test()
    public void whenAddUserThenFail() {
        UserRepository store = new TodoUserRepository(crudRepository);
        User user1 = new User(0, "user1", "login", "123", null);
        User user2 = new User(1, "user2", "login", "321", null);

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);

        List<User> users = store.findAll();

        Assertions.assertNotNull(addResult1);
        Assertions.assertEquals(addResult2, Optional.empty());
        Assertions.assertEquals(users.size(), 1);
        Assertions.assertEquals(addResult1.get().getId(), user1.getId());
        Assertions.assertEquals(addResult1.get().getName(), user1.getName());
        Assertions.assertEquals(addResult1.get().getLogin(), user1.getLogin());
        Assertions.assertEquals(addResult1.get().getPassword(), user1.getPassword());
    }

    @Test()
    public void whenFindUser() {
        UserRepository store = new TodoUserRepository(crudRepository);
        User user1 = new User(0, "user1", "login1", "123", null);
        User user2 = new User(1, "user2", "login2", "321", null);
        store.add(user1);
        store.add(user2);

        Optional<User> result = store.findUser(user2);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.get(), user2);
    }

    private static Stream<Arguments> userProvider() {
        return Stream.of(
                Arguments.of("user1", "login", null),
                Arguments.of("user1", null, "123"),
                Arguments.of("user1", null, null),
                Arguments.of(null, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("userProvider")
    public void whenFindUserFail(String name, String login, String password) {
        UserRepository store = new TodoUserRepository(crudRepository);
        User user = new User(0, name, login, password, null);
        store.add(user);

        Optional<User> result = store.findUser(user);

        Assertions.assertEquals(result, Optional.empty());
    }
}