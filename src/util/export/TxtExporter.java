package util.export;

import model.Task;

import java.util.List;
import java.util.stream.Collectors;

public class TxtExporter extends TaskExporter {

    @Override
    protected String buildContent(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    String taskStr = "TASK: " + task.getTitle() + " | " + task.getDueDate() + " | "
                            + task.getPriority();
                    String subs = task.getSubTasks().stream()
                            .map(sub -> "    - SUB: " + sub.getTitle())
                            .collect(Collectors.joining("\n"));
                    return taskStr + (subs.isEmpty() ? "" : "\n" + subs);
                })
                .collect(Collectors.joining("\n\n"));
    }
}