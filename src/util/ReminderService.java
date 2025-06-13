package util;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.TaskManager;
import model.Task;

public class ReminderService {

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void start(int intervalSeconds) {
        Runnable reminderTask = () -> {
            List<Task> tasks = TaskManager.getInstance().getAllTasks();
            LocalDate today = LocalDate.now();

            if (scheduler == null || scheduler.isShutdown()) {
                scheduler = Executors.newScheduledThreadPool(1);
            }

            System.out.println("[REMINDER] Controllo task in scadenza...");

            boolean found = false;
            for (Task task : tasks) {
                if (!task.isDone() && task.getDueDate() != null
                        && task.getDueDate().isBefore(today.plusDays(1))) {
                    System.out
                            .println("Task urgente: \"" + task.getTitle() + "\" - Scadenza: " + task.getDueDate());
                    found = true;
                }
            }

            if (!found) {
                System.out.println("Nessun task in scadenza nelle prossime 24h.");
            }
            System.out.println();
        };

        scheduler.scheduleAtFixedRate(reminderTask, 0, intervalSeconds, TimeUnit.SECONDS);
        System.out.println("Reminder automatico attivo ogni " + intervalSeconds + " secondi.");
    }

    public void stop() {
        scheduler.shutdown();
    }

}
