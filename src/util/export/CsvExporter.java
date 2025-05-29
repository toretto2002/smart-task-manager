package util.export;

import model.Task;
import model.SubTask;

import java.util.List;

public class CsvExporter extends TaskExporter {

    @Override
    protected String buildContent(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("type,id,title,description,dueDate,priority,done\n");

        for (Task t : tasks) {
            sb.append(String.format("TASK,%s,%s,%s,%s,%s,%s\n",
                    t.getId(), t.getTitle(), t.getDescription(), t.getDueDate(), t.getPriority(), t.isDone()));

            for (SubTask s : t.getSubTasks()) {
                sb.append(String.format("SUBTASK,%s,%s,%s,%s,%s,%s\n",
                        s.getId(), s.getTitle(), s.getDescription(), s.getDueDate(), s.getPriority(), s.isDone()));
            }
        }

        return sb.toString();
    }
}
