package ru.job4j.todo.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.repository.UserRepository;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Сервис пользователей
 */
@ThreadSafe
@Service
public class UserService {
    private final UserRepository store;

    public UserService(UserRepository store) {
        this.store = store;
    }

    /**
     * Добавление нового пользователя
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> add(User user) {
        return store.add(user);
    }

    /**
     * Поиск пользователя по логину
     * @param user - пользователь
     * @return возвращает пользователя
     */
    public Optional<User> findUser(User user) {
        return store.findUser(user);
    }

    /**
     * Получение списка часовых поясов
     * @return часовые пояса
     */
    public List<TimeZone> getTimeZones() {
        List<TimeZone> zones = new ArrayList<>();
        for (String timeId : ZoneId.getAvailableZoneIds()) {
            zones.add(TimeZone.getTimeZone(timeId));
        }
        return zones;
    }

}
