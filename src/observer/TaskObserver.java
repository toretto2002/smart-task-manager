package observer;

import model.Task;

public interface TaskObserver {

    void onTaskCompleted(Task task);
}
