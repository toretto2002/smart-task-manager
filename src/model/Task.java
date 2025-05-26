import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Task {

    private UUID id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean done;
    private List<SubTask> subTasks;

    public Task(String title, String description, LocalDate dueDate, Priority priority) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.done = false;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public boolean isDone() {
        return done;
    }

    public void markAsDone() {
        this.done = true;

        for (SubTask subTask : subTasks) {
            subTask.markAsDone();
        }
    }

    public void addSubTask(SubTask subTask) {
        if (subTasks == null) {
            subTasks = new ArrayList<>();
        }
        subTasks.add(subTask);
    }

    public List<SubTask> getSubTasks() {
        return subTasks != null ? subTasks : new ArrayList<>();
    }

    @Override
    public String toString() {
        return "[" + priority + "] " + title + " (due: " + dueDate + ") - " + (done ? "DONE" : "TODO");
    }

}
