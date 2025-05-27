package model;

import java.time.LocalDate;

public class TaskFactory {

    public static Task createTask(String title, String description, LocalDate dueDate, Priority priority) {
        return new Task(title, description, dueDate, priority);
    }

    public static SubTask createSubTask(String title, String description, LocalDate dueDate, Priority priority) {
        return new SubTask(title, description, dueDate, priority);
    }

}
