package util;

import model.Task;
import model.SubTask;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class IOHandler {

    private static final Logger logger = Logger.getLogger(IOHandler.class.getName());

    public static void saveTasks(List<Task> tasks, String path) {

        // Dichiaro le risorse necessarie per la scrittura su file dentro le parentesi
        // del try-with-resources per garantire che vengano chiuse automaticamente alla
        // fine del blocco
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {

            for (Task task : tasks) {
                // Scrivo ogni task su una nuova riga nel file
                writer.write(task.toString());
                writer.newLine();
            }

        } catch (IOException e) {
            logger.severe("Error saving tasks to file: " + e.getMessage());
        }
    }

}
