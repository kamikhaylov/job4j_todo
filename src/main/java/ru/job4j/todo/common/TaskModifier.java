package ru.job4j.todo.common;

import org.apache.commons.lang3.StringUtils;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Модификатор задач
 */
public class TaskModifier {

    private static final TimeZone DEFAULT_ZONE = TimeZone.getDefault();

    public Task modify(Task task, String timezone) {
        modifyCreated(task, timezone);
        return task;
    }

    private void modifyCreated(Task task, String timezone) {
        if (StringUtils.isEmpty(timezone)) {
            timezone = DEFAULT_ZONE.getID();
        }
        LocalDateTime date = task.getCreated()
                .atZone(DEFAULT_ZONE.toZoneId())
                .withZoneSameInstant(ZoneId.of(timezone))
                .toLocalDateTime();

        task.setCreated(date);
    }
}
