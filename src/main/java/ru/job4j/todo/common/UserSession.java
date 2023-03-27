package ru.job4j.todo.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Пользовательская сессия
 */
public final class UserSession {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserSession.class.getName());
    private static final String USER = "user";
    private static final String GUEST = "guest";

    private UserSession() {
    }

    /**
     * Создание пользовательской сессии
     * @param user - пользователь
     * @param req - запрос
     */
    public static void create(User user, HttpServletRequest req) {
        LOGGER.info("Создание пользовательской сессии");

        HttpSession session = req.getSession();
        session.setAttribute(USER, user);
    }

    /**
     * Проверка пользователя
     * @param model - модель атрибутов
     * @param httpSession - http-сессия
     * @return возвращает пользователя
     */
    public static User getUser(Model model, HttpSession httpSession) {
        LOGGER.info("Проверка пользователя");

        User user = (User) httpSession.getAttribute(USER);
        if (user == null) {
            user = new User();
            user.setName(GUEST);
        }
        model.addAttribute(USER, user);

        return user;
    }

    /**
     * Сброс сессии
     * @param httpSession - http-сессия
     */
    public static void invalidate(HttpSession httpSession) {
        LOGGER.info("Сброс сессии");

        httpSession.invalidate();
    }
}
