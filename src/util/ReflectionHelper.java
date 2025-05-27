package util;

import java.lang.reflect.Field;
import java.util.UUID;

import model.Task;

public class ReflectionHelper {

    public static void setId(Task task, UUID id) {
        try {
            Field field = task.getClass().getDeclaredField("id");
            field.setAccessible(true); // Rende il campo accessibile anche se privato
            field.set(task, id);
        } catch (NoSuchFieldException e) {
            System.err.println("Field 'id' not found in Task class: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("Cannot access field 'id': " + e.getMessage());
        }
    }

}
