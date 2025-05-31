package observer;

import model.Task;

public class ConsoleNotifier implements TaskObserver {

    @Override
    public void onTaskCompleted(Task task) {
        System.out.println("Notifica: Il task \"" + task.getTitle() + "\" è stato completato.");
    }

}
