package model;

import java.time.LocalDate;

public class SubTask extends Task {

    public SubTask(String title, String description, LocalDate dueDate, Priority priority) {
        super(title, description, dueDate, priority);
    }
}
