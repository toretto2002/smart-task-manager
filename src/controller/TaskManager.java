import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import util.LoggerUtil;

public class TaskManager {

    private static TaskManager instance;
    private final List<Task> tasks;
    private static final Logger logger = LoggerUtil.getLogger(TaskManager.class);

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

}
