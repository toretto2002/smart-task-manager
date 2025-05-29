package util.export;

import java.util.List;

import model.Task;

public abstract class TaskExporter {

    public final void export(List<Task> tasks, String path) {
        String content = buildContent(tasks);
        writeToFile(content, path);
    }

    protected abstract String buildContent(List<Task> tasks);

    private void writeToFile(String content, String path) {
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(path))) {
            writer.write(content);
        } catch (Exception e) {
            System.err.println("Errore durante l'esportazione: " + e.getMessage());
        }
    }

}
