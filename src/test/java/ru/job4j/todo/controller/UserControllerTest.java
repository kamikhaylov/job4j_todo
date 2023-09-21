package ru.job4j.todo.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {
    private static final int ID_TEST = 1;
    private static final String LOGIN_TEST = "test@test.ru";
    private static final String PASS_TEST = "123";
    private static final String NAME_TEST = "name";

    @Mock
    private Model model;
    @Mock
    private UserService service;
    @Mock
    private HttpSession httpSession;
    @Mock
    private HttpServletRequest req;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, service, httpSession, req);
    }

    @Test
    public void whenAddUser() {
        UserController userController = new UserController(service);
        String page = userController.addUser(model);

        verify(model).addAttribute("user", new User(0, "", "", "", null));
        Assertions.assertEquals(page, "users/create");
    }

    @Test
    public void whenRegistrationFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);
        when(service.add(user)).thenReturn(Optional.empty());

        String page = userController.registration(model, user);

        verify(service).add(user);
        verify(model).addAttribute("message", "");
        Assertions.assertEquals(page, "redirect:/users/fail");
    }

    @Test
    public void whenRegistrationSuccess() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);
        when(service.add(user)).thenReturn(Optional.of(user));

        String page = userController.registration(model, user);

        verify(service).add(user);
        Assertions.assertEquals(page, "redirect:/users/success");
    }

    @Test
    public void whenSuccess() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);

        String page = userController.success(user);

        Assertions.assertEquals(page, "users/success");
    }

    @Test
    public void whenFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);

        String page = userController.fail(user);

        Assertions.assertEquals(page, "users/fail");
    }

    @Test
    public void whenLoginPage() {
        UserController userController = new UserController(service);

        String page = userController.auth(model, true);

        verify(model).addAttribute("fail", true);
        Assertions.assertEquals(page, "users/login");
    }

    @Test
    public void whenLoginFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);
        when(service.findUser(user)).thenReturn(Optional.empty());

        String page = userController.login(user, req);

        verify(service).findUser(user);
        Assertions.assertEquals(page, "redirect:/users/auth?fail=true");
    }

    @Test
    public void whenLogin() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST, null);
        when(service.findUser(user)).thenReturn(Optional.of(user));
        when(req.getSession()).thenReturn(httpSession);

        String page = userController.login(user, req);

        verify(service).findUser(user);
        Assertions.assertEquals(page, "redirect:/tasks/all");
    }

    @Test
    public void whenLogout() {
        UserController userController = new UserController(service);

        String page = userController.logout(httpSession);

        verify(httpSession).invalidate();
        Assertions.assertEquals(page, "redirect:/users/auth");
    }
}