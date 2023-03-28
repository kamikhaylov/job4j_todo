package ru.job4j.todo.controller;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
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
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class.getName());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model) {
        LOGGER.info("Добавление пользователя");
        model.addAttribute("user", new User(0, "", "", ""));
        return "createUser";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        LOGGER.info("Регистрация");

        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            model.addAttribute("message", "");
            return "redirect:/userFail";
        }
        return "redirect:/userSuccess";
    }

    @GetMapping("/userSuccess")
    public String userSuccess(@ModelAttribute User user) {
        LOGGER.info("Успешная регистрация");
        return "userSuccess";
    }

    @GetMapping("/userFail")
    public String userFail(@ModelAttribute User user) {
        LOGGER.info("Ошибка в регистрации");
        return "userFail";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model,
                            @RequestParam(name = "fail", required = false) Boolean fail) {
        LOGGER.info("Страница с регистрацией");
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        LOGGER.info("Логин");
        Optional<User> userDb = userService.findUser(user);
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        UserSession.create(userDb.get(), req);
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        UserSession.invalidate(session);
        return "redirect:/loginPage";
    }
}
