import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToDo {
    private static final String FILE_NAME = "data.txt";
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        loadData();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to ToDo. Enter 'help' for instructions.");

        boolean shouldExit = false;
        while (!shouldExit) {
            System.out.print("> ");
            String command = scanner.nextLine();
            processCommand(command);
        }
    }

    private static void processCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String action = parts[0].toLowerCase();

        switch (action) {
            case "add":
                addTask(parts);
                break;
            case "view":
                viewTasks();
                break;
            case "delete":
                deleteTask(parts);
                break;
            case "complete":
                markComplete(parts);
                break;
            case "incomplete":
                markIncomplete(parts);
                break;
            case "help":
                displayHelp();
                break;
            case "exit":
                saveData();
                System.exit(0);
            default:
                System.out.println("Invalid command. Enter 'help' for instructions.");
        }
    }

    private static void addTask(String[] parts) {
        if (parts.length == 2) {
            Task task = new Task(parts[1]);
            tasks.add(task);
            System.out.println("Task \"" + task.getDescription() + "\" has been added to the list.");
            saveData();
        } else {
            System.out.println("Invalid command. Use 'add <task_description>'.");
        }
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks to display.");
        } else {
            System.out.printf("%-10s%-20s%s%n", "       ", "Status", "Description");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.printf("%-10d%-20s%s%n",
                        i + 1,
                        task.isCompleted() ? "complete" : "incomplete",
                        task.getDescription());
            }
        }
    }

    private static void deleteTask(String[] parts) {
        if (parts.length == 2) {
            try {
                int position = Integer.parseInt(parts[1]);
                if (position > 0 && position <= tasks.size()) {
                    Task deletedTask = tasks.remove(position - 1);
                    System.out.println("Task " + position + " has been deleted.");
                    saveData();
                } else {
                    System.out.println("Invalid task position.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid command. Use 'delete <task_position>'.");
            }
        } else {
            System.out.println("Invalid command. Use 'delete <task_position>'.");
        }
    }

    private static void markComplete(String[] parts) {
        markTask(parts, true);
    }

    private static void markIncomplete(String[] parts) {
        markTask(parts, false);
    }

    private static void markTask(String[] parts, boolean complete) {
        if (parts.length == 2) {
            try {
                int position = Integer.parseInt(parts[1]);
                if (position > 0 && position <= tasks.size()) {
                    Task task = tasks.get(position - 1);
                    if (complete) {
                        task.markComplete();
                        System.out.println("Task " + position + " has been marked as complete.");
                    } else {
                        task.markIncomplete();
                        System.out.println("Task " + position + " has been marked as incomplete.");
                    }
                    saveData();
                } else {
                    System.out.println("Invalid task position.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid command. Use '" + (complete ? "complete" : "incomplete") + " <task_position>'.");
            }
        } else {
            System.out.println("Invalid command. Use '" + (complete ? "complete" : "incomplete") + " <task_position>'.");
        }
    }

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  add <task_description> - Adds a task");
        System.out.println("  view - Displays tasks in a neat table format");
        System.out.println("  delete <task_position> - Deletes a task");
        System.out.println("  complete <task_position> - Marks a task as complete");
        System.out.println("  incomplete <task_position> - Marks a task as incomplete");
        System.out.println("  help - Displays clear usage instructions");
        System.out.println("  exit - Exits the application");
    }

    private static void loadData() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                Task task = new Task(line);
                tasks.add(task);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveData() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (Task task : tasks) {
                writer.write(task.getDescription());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }
}