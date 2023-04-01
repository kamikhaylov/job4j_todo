package ru.job4j.todo.common;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserSessionTest {
    @Mock
    private Model model;
    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, httpSession);
    }

    @Test
    public void whenGetUser() {
        User user = new User(0, "user1", "login1", "123");
        when(httpSession.getAttribute("user")).thenReturn(user);

        User actual = UserSession.getUser(model, httpSession);

        verify(model).addAttribute("user", user);
        Assertions.assertEquals(actual, user);
    }

    @Test
    public void whenGetGuest() {
        User user = new User(0, "guest", null, null);
        when(httpSession.getAttribute("user")).thenReturn(null);

        User actual = UserSession.getUser(model, httpSession);

        verify(model).addAttribute("user", user);
        Assertions.assertEquals(actual, user);
    }
}