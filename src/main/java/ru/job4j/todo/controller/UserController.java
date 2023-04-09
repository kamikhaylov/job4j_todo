package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.common.UserSession;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Контроллер пользователей
 */
@ThreadSafe
@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAdd")
    public String addUser(Model model) {
        LOGGER.info("Добавление пользователя");
        model.addAttribute("user", new User(0, "", "", ""));
        return "users/create";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        LOGGER.info("Регистрация");

        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "");
            return "redirect:/users/fail";
        }
        return "redirect:/users/success";
    }

    @GetMapping("/success")
    public String success(@ModelAttribute User user) {
        LOGGER.info("Успешная регистрация");
        return "users/success";
    }

    @GetMapping("/fail")
    public String fail(@ModelAttribute User user) {
        LOGGER.info("Ошибка в регистрации");
        return "users/fail";
    }

    @GetMapping("/auth")
    public String auth(Model model,
                       @RequestParam(name = "fail", required = false) Boolean fail) {
        LOGGER.info("Страница с регистрацией");
        model.addAttribute("fail", fail != null);
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        LOGGER.info("Логин");
        Optional<User> userDb = userService.findUser(user);
        if (userDb.isEmpty()) {
            return "redirect:/users/auth?fail=true";
        }
        UserSession.create(userDb.get(), req);
        return "redirect:/tasks/all";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserSession.invalidate(session);
        return "redirect:/users/auth";
    }
}
