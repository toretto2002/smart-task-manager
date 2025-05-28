package util;

import model.Task;
import model.Priority;
import model.SubTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class IOHandler {

    private static final Logger logger = Logger.getLogger(IOHandler.class.getName());

    public static void saveTasks(List<Task> tasks, String path) {

        // Dichiaro le risorse necessarie per la scrittura su file dentro le parentesi
        // del try-with-resources per garantire che vengano chiuse automaticamente alla
        // fine del blocco
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

            for (Task task : tasks) {
                writer.write(String.format("TASK|%s|%s|%s|%s|%s|%s",
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getDueDate(),
                        task.getPriority(),
                        task.isDone()));
                writer.newLine();

                for (SubTask subTask : task.getSubTasks()) {
                    writer.write(String.format("SUBTASK|%s|%s|%s|%s|%s",
                            subTask.getId(),
                            subTask.getTitle(),
                            subTask.getDescription(),
                            subTask.getDueDate(),
                            subTask.isDone()));
                    writer.newLine();
                }

            }

            logger.info("Tasks saved successfully to " + path);

        } catch (IOException e) {
            logger.severe("Error saving tasks to file: " + e.getMessage());
        }
    }

    public static List<Task> loadTasks(String path) {
        List<Task> tasks = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

            String line;
            Task currentTask = null;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("\\|");

                if (parts[0].equals("TASK")) {
                    UUID id = UUID.fromString(parts[1]);
                    String title = parts[2];
                    String description = parts[3];
                    LocalDate dueDate = LocalDate.parse(parts[4]);
                    Priority priority = Priority.valueOf(parts[5]);
                    boolean isDone = Boolean.parseBoolean(parts[6]);

                    currentTask = new Task(title, description, dueDate, priority);
                    if (isDone) {
                        currentTask.markAsDone();
                    }

                    ReflectionHelper.setId(currentTask, id);

                    tasks.add(currentTask);
                } else if (parts[0].equals("SUBTASK") && currentTask != null) {
                    UUID subId = UUID.fromString(parts[1]);
                    String title = parts[2];
                    String description = parts[3];
                    LocalDate dueDate = LocalDate.parse(parts[4]);
                    Priority priority = Priority.valueOf(parts[5]);
                    boolean isDone = Boolean.parseBoolean(parts[6]);

                    SubTask subTask = new SubTask(title, description, dueDate, priority);
                    if (isDone) {
                        subTask.markAsDone();
                    }
                    ReflectionHelper.setId(subTask, subId);

                    currentTask.addSubTask(subTask);
                }
            }

            logger.info("Tasks loaded successfully from " + path);
        } catch (IOException e) {
            logger.warning("Error loading tasks from file: " + e.getMessage());
        }
        return tasks;
    }

}
