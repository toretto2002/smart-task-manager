import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.function.Predicate;

import controller.TaskManager;
import iterator.TaskIterator;
import model.Priority;
import model.SubTask;
import model.Task;
import model.TaskBuilder;
import model.TaskFactory;
import util.ConfigLoader;
import util.IOHandler;
import util.Validator;
import util.export.CsvExporter;
import util.export.TaskExporter;
import util.export.TxtExporter;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = TaskManager.getInstance();

    private static boolean tasksLoaded = false;

    public static void main(String[] args) {
        boolean exit = false;
        System.out.println("Ciao utente benvenuto nel task manager perfetto per gestire i tuoi task quotidiani!");
        while (!exit) {

            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> createTask();
                case "2" -> showAllTasks();
                case "3" -> showUrgentTasks();
                case "4" -> showCompletedTasks();
                case "5" -> addSubTaskToTask();
                case "6" -> saveTasksToFile();
                case "7" -> loadTasksFromFile();
                case "8" -> markTaskOrSubTaskAsDone();
                case "9" -> exportTasksAsTxt();
                case "10" -> exportTasksAsCsv();
                case "0" -> exit = true;
                default -> System.out.println("Scelta non valida.");
            }

        }
        System.out.println("");
    }

    private static void printMenu() {
        System.out.println("\n=== SMART TASK MANAGER ===");
        System.out.println("1. Crea nuovo Task");
        System.out.println("2. Visualizza tutti i Task");
        System.out.println("3. Visualizza Task urgenti");
        System.out.println("4. Visualizza Task completati");
        System.out.println("5. Aggiungi sotto-Task a un Task");
        System.out.println("6. Salva Task su file");
        System.out.println("7. Carica Task da file");
        System.out.println("8. Completa un Task o Sotto-task");
        System.out.println("9. Esporta task in formato TXT");
        System.out.println("10. Esporta task in formato CSV");
        System.out.println("0. Esci");
        System.out.print("Scelta: ");
    }

    private static void createTask() {

        String title = readValidString("Inserisci il titolo del Task: ");

        String description = readValidString("Inserisci la descrizione del Task: ");

        LocalDate dueDate = readValidDate("Inserisci la data di scadenza (YYYY-MM-DD): ");

        Priority priority = readValidPriority("Priorità (LOW, MEDIUM, HIGH): ");

        Task task = new TaskBuilder()
                .setTitle(title)
                .setDescription(description)
                .setDueDate(dueDate)
                .setPriority(priority)
                .build();

        taskManager.addTask(task);
        System.out.println("Task creato con successo!");

        return;
    }

    private static void loadTasksFromFile() {
        String path = ConfigLoader.get("task.file", "data/tasks.txt");
        List<Task> tasks = IOHandler.loadTasks(path);
        int importedCount = 0;

        for (Task newTask : tasks) {
            boolean alreadyExists = TaskManager.getInstance().getAllTasks().stream()
                    .anyMatch(existing -> existing.getId().equals(newTask.getId()));
            if (!alreadyExists) {
                TaskManager.getInstance().addTask(newTask);
                importedCount++;
            }
        }
        tasksLoaded = true;
        System.out.println("Task caricati con successo da fileTask aggiunti: " + importedCount);
    }

    private static void saveTasksToFile() {
        if (!tasksLoaded) {
            System.out.print("Non hai caricato i task da file. Vuoi sovrascrivere comunque? (s/N): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (!input.equals("s")) {
                System.out.println("Operazione annullata.");
                return;
            }
        }

        String path = ConfigLoader.get("task.file", "data/tasks.txt");
        if (taskManager.getAllTasks().isEmpty()) {
            System.out.println("Nessun task da salvare.");
            return;
        }
        IOHandler.saveTasks(taskManager.getAllTasks(), path);
    }

    private static void addSubTaskToTask() {
        showAllTasksWithId(true);

        UUID taskId = readValidUUID("Inserisci l'ID del Task a cui aggiungere un sotto-task: ");

        Task mainTask = taskManager.getTaskById(taskId).orElse(null);
        if (mainTask == null) {
            System.out.println("Task non trovato con ID: " + taskId);
            return;
        }

        String title = readValidString("Titolo del sotto-task: ");

        String desc = readValidString("Descrizione: ");

        LocalDate date = readValidDate("Data di scadenza (YYYY-MM-DD): ");

        Priority prio = readValidPriority("Priorità (LOW, MEDIUM, HIGH): ");

        SubTask sub = TaskFactory.createSubTask(title, desc, date, prio);

        System.out.println("Sotto-task creato: " + sub);
        mainTask.addSubTask(sub);

        System.out.println("Sotto-task aggiunto.");

    }

    private static void showCompletedTasks() {
        Predicate<Task> filter = Task::isDone;
        TaskIterator taskIterator = new TaskIterator(taskManager.getAllTasks(), filter);
        System.out.println("=== Elenco di tutti i Task completati ===");
        if (!taskIterator.hasNext()) {
            System.out.println("Nessun task completato trovato.");
        } else {
            while (taskIterator.hasNext()) {
                printTask(taskIterator.next());
            }
        }
    }

    private static void showUrgentTasks() {
        Predicate<Task> filter = task -> task.getPriority() == Priority.HIGH && !task.isDone();
        TaskIterator taskIterator = new TaskIterator(taskManager.getAllTasks(), filter);
        System.out.println("=== Elenco di tutti i Task urgenti ===");
        if (!taskIterator.hasNext()) {
            System.out.println("Nessun task urgente trovato.");
        } else {
            while (taskIterator.hasNext()) {
                printTask(taskIterator.next());
            }
        }
    }

    private static void showAllTasks() {
        System.out.println("=== Elenco di tutti i Task ===");
        List<Task> tasks = taskManager.getAllTasks();
        printTasks(tasks);
        return;
    }

    private static void showAllTasksWithId() {
        showAllTasksWithId(false); // chiama la versione avanzata con il valore default
    }

    private static void showAllTasksWithId(boolean filterIncomplete) {
        System.out.println("=== Elenco di tutti i Task con ID ===");
        List<Task> tasks = taskManager.getAllTasks();

        if (filterIncomplete) {
            tasks = tasks.stream().filter(t -> !t.isDone()).toList();
        }

        if (tasks.isEmpty()) {
            System.out.println("Nessun task trovato.");
        } else {
            for (Task task : tasks) {
                System.out.println(task.getId() + " - " + task.getTitle());
            }
        }
    }

    private static void printTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("Nessun task trovato.");
        } else {
            for (Task task : tasks) {
                printTask(task);
            }
        }
    }

    private static void printTask(Task task) {
        System.out.println(task);
        for (SubTask subTask : task.getSubTasks()) {
            System.out.println("  - " + subTask);
        }
    }

    private static void markTaskOrSubTaskAsDone() {
        showAllTasksWithId(true); // mostra solo i task non completati

        if (taskManager.getAllTasks().isEmpty()) {
            return;
        }

        System.out.print("Inserisci ID del task da completare: ");
        UUID id = UUID.fromString(scanner.nextLine());

        Task task = taskManager.getTaskById(id).orElse(null);
        if (task == null) {
            System.out.println("Task non trovato.");
            return;
        }

        if (!task.hasSubTasks()) {
            task.markAsDone();
            System.out.println("Task completato.");
            return;
        }

        // Task ha sottotask: chiedi cosa fare
        System.out.println("Il task ha dei sotto-task. Cosa vuoi fare?");
        System.out.println("1. Completare tutto (task e sotto-task)");
        System.out.println("2. Completare solo un sotto-task");

        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            task.markAsDone(); // già propaga sui subtask
            System.out.println("Task e tutti i sotto-task completati.");
        } else if (choice.equals("2")) {
            List<SubTask> subs = task.getSubTasks();
            for (int i = 0; i < subs.size(); i++) {
                System.out.println((i + 1) + ". " + subs.get(i));
            }

            int index = readValidIndex("Inserisci il numero del sotto-task da completare: ", subs.size());

            if (index >= 0 && index < subs.size()) {
                subs.get(index).markAsDone();
                System.out.println("Sotto-task completato.");

                if (task.areAllSubTasksDone()) {
                    task.markOnlySelfAsDone(); // completamento automatico
                    System.out.println("Tutti i sotto-task completati → Task completato automaticamente.");
                }
            } else {
                System.out.println("Selezione non valida.");
            }
        } else {
            System.out.println("Scelta non valida.");
        }
    }

    private static LocalDate readValidDate(String prompt) {
        System.out.print(prompt);
        String dateStr = scanner.nextLine();
        while (!Validator.isValidDate(dateStr)) {
            System.out.print("Data non valida. Riprova (YYYY-MM-DD): ");
            dateStr = scanner.nextLine();
        }
        return LocalDate.parse(dateStr);
    }

    private static Priority readValidPriority(String prompt) {
        System.out.print(prompt);
        String prioStr = scanner.nextLine();
        while (!Validator.isValidPriority(prioStr)) {
            System.out.print("Priorità non valida. Riprova (LOW, MEDIUM, HIGH): ");
            prioStr = scanner.nextLine();
        }
        return Priority.valueOf(prioStr.toUpperCase());
    }

    private static UUID readValidUUID(String prompt) {
        System.out.print(prompt);
        String idStr = scanner.nextLine();
        while (!Validator.isValidUUID(idStr)) {
            System.out.print("UUID non valido. Riprova: ");
            idStr = scanner.nextLine();
        }
        return UUID.fromString(idStr);
    }

    private static int readValidIndex(String prompt, int maxSize) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (!Validator.isValidIndex(input, maxSize)) {
            System.out.print("Inserimento non valido. Riprova: ");
            input = scanner.nextLine();
        }
        return Integer.parseInt(input) - 1; // -1 perché le liste partono da 0
    }

    private static String readValidString(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine();
        while (!Validator.isValidString(input)) {
            System.out.print("Il campo non può essere vuoto. Riprova: ");
            input = scanner.nextLine();
        }
        return input;
    }

    private static void exportTasksAsTxt() {
        String path = ConfigLoader.get("export.file.txt", "exports/export-txt.txt");
        TaskExporter exporter = new TxtExporter();
        exporter.export(taskManager.getAllTasks(), path);
        System.out.println("Esportazione completata: " + path);
    }

    private static void exportTasksAsCsv() {
        String path = ConfigLoader.get("export.file.csv", "exports/export-csv.csv");
        TaskExporter exporter = new CsvExporter();
        exporter.export(taskManager.getAllTasks(), path);
        System.out.println("Task esportati in CSV in: " + path);
    }

}
