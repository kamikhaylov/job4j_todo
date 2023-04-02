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

        verify(model).addAttribute("user", new User(0, "", "", ""));
        Assertions.assertEquals(page, "createUser");
    }

    @Test
    public void whenRegistrationFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);
        when(service.add(user)).thenReturn(Optional.empty());

        String page = userController.registration(model, user);

        verify(service).add(user);
        verify(model).addAttribute("message", "");
        Assertions.assertEquals(page, "redirect:/userFail");
    }

    @Test
    public void whenRegistrationSuccess() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);
        when(service.add(user)).thenReturn(Optional.of(user));

        String page = userController.registration(model, user);

        verify(service).add(user);
        Assertions.assertEquals(page, "redirect:/userSuccess");
    }

    @Test
    public void whenUserSuccess() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);

        String page = userController.userSuccess(user);

        Assertions.assertEquals(page, "userSuccess");
    }

    @Test
    public void whenUserFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);

        String page = userController.userFail(user);

        Assertions.assertEquals(page, "userFail");
    }

    @Test
    public void whenLoginPage() {
        UserController userController = new UserController(service);

        String page = userController.loginPage(model, true);

        verify(model).addAttribute("fail", true);
        Assertions.assertEquals(page, "login");
    }

    @Test
    public void whenLoginFail() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);
        when(service.findUser(user)).thenReturn(Optional.empty());

        String page = userController.login(user, req);

        verify(service).findUser(user);
        Assertions.assertEquals(page, "redirect:/loginPage?fail=true");
    }

    @Test
    public void whenLogin() {
        UserController userController = new UserController(service);
        User user = new User(ID_TEST, NAME_TEST, LOGIN_TEST, PASS_TEST);
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
        Assertions.assertEquals(page, "redirect:/loginPage");
    }
}