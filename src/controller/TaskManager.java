package controller;

import model.Task;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import util.LoggerUtil;

public class TaskManager {

    private static TaskManager instance;
    private final List<Task> tasks;
    private static final Logger logger = LoggerUtil.getLogger(TaskManager.class);

    // metodo per ottenere istanza singleton del TaskManager
    private TaskManager() {
        this.tasks = new ArrayList<>();
        logger.info("TaskManager initialized.");
    }

    public static TaskManager getInstance() {
        if (instance == null) {
            synchronized (TaskManager.class) { // sincronizza l'accesso per evitare problemi in caso di accessi
                                               // concorrenti in caso di multi-threading
                if (instance == null) {
                    instance = new TaskManager();
                }
            }
        }
        return instance;
    }

    public void addTask(Task task) {
        if (task != null) {
            tasks.add(task);
            logger.info("Task added: " + task.getTitle());
        } else {
            logger.warning("Attempted to add a null task.");
        }
    }

    public boolean removeTask(UUID id) {

        boolean removed = tasks.removeIf(task -> task.getId().equals(id));
        if (removed) {
            logger.info("Task removed with ID: " + id);
        } else {
            logger.warning("No task found with ID: " + id);
        }

        return removed;
    }

    public List<Task> getAllTasks() {
        logger.info("Retrieving all tasks. Total tasks: " + tasks.size());
        return new ArrayList<>(tasks);
    }

    public List<Task> getCompletedTasks() {
        return tasks.stream().filter(Task::isDone).collect(Collectors.toList());
    }

    public List<Task> getTasksDueBefore(LocalDate date) {
        List<Task> dueTasks = tasks.stream()
                .filter(task -> task.getDueDate() != null && task.getDueDate().isBefore(date) && !task.isDone())
                .collect(Collectors.toList());
        logger.info("Retrieved " + dueTasks.size() + " tasks due before " + date);
        return dueTasks;
    }

    public Optional<Task> getTaskById(UUID id) {
        return tasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    public void clearAllTasks() {
        tasks.clear();
        logger.info("All tasks cleared.");
    }

    public void importTasks(List<Task> importedTasks) {
        if (importedTasks != null && !importedTasks.isEmpty()) {
            tasks.addAll(importedTasks);
            logger.info("Imported " + importedTasks.size() + " tasks.");
        } else {
            logger.warning("Attempted to import an empty or null task list.");
        }
    }

}
