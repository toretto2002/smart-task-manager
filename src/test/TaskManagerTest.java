package test;

import controller.TaskManager;
import model.*;
import util.IOHandler;

import org.junit.jupiter.api.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    private TaskManager manager;

    @BeforeEach
    public void setUp() {
        manager = TaskManager.getInstance();
        manager.clearAllTasks();
    }

    @Test
    public void testAddAndRemoveTask() {
        Task task = new Task("Test Add", "Testing addTask", LocalDate.now(), Priority.MEDIUM);
        manager.addTask(task);

        List<Task> allTasks = manager.getAllTasks();
        assertEquals(1, allTasks.size(), "Il task dovrebbe essere stato aggiunto");

        boolean removed = manager.removeTask(task.getId());
        assertTrue(removed, "Il task dovrebbe essere stato rimosso");
        assertTrue(manager.getAllTasks().isEmpty(), "La lista dovrebbe essere vuota");
    }

    @Test
    public void testMarkAsDoneWithSubTasks() {
        Task task = new Task("Main Task", "Testing composite", LocalDate.now(), Priority.HIGH);
        SubTask sub1 = new SubTask("Sub1", "Detail 1", LocalDate.now(), Priority.LOW);
        SubTask sub2 = new SubTask("Sub2", "Detail 2", LocalDate.now(), Priority.LOW);

        task.addSubTask(sub1);
        task.addSubTask(sub2);
        task.markAsDone();

        assertTrue(task.isDone(), "Il task principale dovrebbe essere completato");
        assertTrue(sub1.isDone(), "Il sotto-task 1 dovrebbe essere completato");
        assertTrue(sub2.isDone(), "Il sotto-task 2 dovrebbe essere completato");
    }

    @Test
    public void testSaveAndLoadTasks() {
        Task task = new Task("SaveTest", "Test IOHandler", LocalDate.now(), Priority.HIGH);
        SubTask sub = new SubTask("SubSaved", "Test inner", LocalDate.now(), Priority.LOW);
        task.addSubTask(sub);

        String path = "test-output-tasks.txt";
        IOHandler.saveTasks(List.of(task), path);
        List<Task> loaded = IOHandler.loadTasks(path);

        assertEquals(1, loaded.size(), "Dovrebbe esserci un task caricato");
        Task loadedTask = loaded.get(0);
        assertEquals(task.getTitle(), loadedTask.getTitle(), "Titolo del task caricato errato");
        assertEquals(1, loadedTask.getSubTasks().size(), "Dovrebbe esserci un sotto-task");
        assertEquals(sub.getTitle(), loadedTask.getSubTasks().get(0).getTitle(), "Titolo del sotto-task errato");

        new File(path).delete(); // cleanup
    }

    @Test
    public void testInvalidInputsInValidator() {
        assertFalse(util.Validator.isValidUUID("abc123"), "UUID invalido dovrebbe fallire");
        assertFalse(util.Validator.isValidDate("not-a-date"), "Data non valida dovrebbe fallire");
        assertFalse(util.Validator.isValidPriority("urgentissimo"), "Priorità invalida dovrebbe fallire");
        assertFalse(util.Validator.isValidIndex("5", 3), "Indice fuori range dovrebbe fallire");
        assertFalse(util.Validator.isValidString("   "), "Stringa vuota dovrebbe fallire");
    }

    @Test
    public void testTaskIteratorWithPriorityFilter() {
        Task task1 = new Task("Alta", "Urgente", LocalDate.now(), Priority.HIGH);
        Task task2 = new Task("Media", "Normale", LocalDate.now(), Priority.MEDIUM);
        Task task3 = new Task("Alta2", "Urgente2", LocalDate.now(), Priority.HIGH);

        List<Task> all = List.of(task1, task2, task3);
        Predicate<Task> highPriority = t -> t.getPriority() == Priority.HIGH;

        iterator.TaskIterator iter = new iterator.TaskIterator(all, highPriority);

        int count = 0;
        while (iter.hasNext()) {
            Task t = iter.next();
            assertEquals(Priority.HIGH, t.getPriority());
            count++;
        }

        assertEquals(2, count, "Dovrebbero esserci due task ad alta priorità");
    }
}