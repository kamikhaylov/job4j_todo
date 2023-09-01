package ru.job4j.todo.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;

class TaskModifierTest {

    private static final LocalDateTime DATE = LocalDateTime.now();
    private static final Task TASK = new Task(1, "description", DATE,
            false, new User(), null, null);

    private final TaskModifier taskModifier = new TaskModifier();

    @Test
    public void whenTimezoneIsEmpty() {
        Task result = taskModifier.modify(TASK, null);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCreated(), TASK.getCreated());
    }

    @Test
    public void whenTimezoneIsNotEmpty() {
        Task result = taskModifier.modify(TASK, "Etc/GMT-14");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCreated(), TASK.getCreated());
    }
}