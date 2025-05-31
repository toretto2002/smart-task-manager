package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import observer.TaskObserver;

public class Task {

    private UUID id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean done;
    private List<SubTask> subTasks;

    private transient List<TaskObserver> observers = new ArrayList<>();

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
        if (subTasks == null) {
            notifyObservers();
            return; // No sub-tasks to mark as done
        }
        for (SubTask subTask : subTasks) {
            subTask.markAsDone();
        }
        notifyObservers();

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

    public boolean hasSubTasks() {
        return subTasks != null && !subTasks.isEmpty();
    }

    public void markOnlySelfAsDone() {
        this.done = true;
    }

    public boolean areAllSubTasksDone() {
        return subTasks.stream().allMatch(SubTask::isDone);
    }

    @Override
    public String toString() {
        return "[" + priority + "] " + title + " (due: " + dueDate + ") - " + (done ? "DONE" : "TODO");
    }

    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (TaskObserver observer : observers) {
            observer.onTaskCompleted(this);
        }
    }

}
