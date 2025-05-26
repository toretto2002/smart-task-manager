import java.time.LocalDate;

public class TaskBuilder {

    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;

    public TaskBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public TaskBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TaskBuilder setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public TaskBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    public Task build() {
        return new Task(title, description, dueDate, priority);
    }

}
