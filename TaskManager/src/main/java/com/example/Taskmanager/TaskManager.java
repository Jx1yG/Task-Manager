package com.example.Taskmanager;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;

public class TaskManager extends Application {
    private static final String FILE_NAME = "data.txt";
    private static final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private final FilteredList<Task> filteredTasks = new FilteredList<>(tasks, task -> true);
    private final TextField taskInput = new TextField();
    private TableView<Task> tableView;

    public static void main(String[] args) {
        loadData();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("To-Do List Manager");
        Scene scene = createScene();
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Scene createScene() {
        // Create TableView for displaying tasks
        tableView = new TableView<>();
        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Task, Boolean> completedColumn = new TableColumn<>("Status");
        tableView.getColumns().addAll(descriptionColumn, completedColumn);
        completedColumn.setCellFactory(CheckBoxTableCell.forTableColumn(completedColumn));
        completedColumn.setCellValueFactory(cellData -> cellData.getValue().completedProperty());

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> handleAddTask(taskInput.getText()));
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> handleDeleteTask(tableView.getSelectionModel().getSelectedItem()));
        Button completeButton = new Button("Mark Complete");
        completeButton.setOnAction(e -> handleMarkComplete(tableView.getSelectionModel().getSelectedItem()));
        Button incompleteButton = new Button("Mark Incomplete");
        incompleteButton.setOnAction(e -> handleMarkIncomplete(tableView.getSelectionModel().getSelectedItem()));

        tableView.setItems(filteredTasks);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        VBox root = new VBox(tableView, createInputBox(), createButtonBar(addButton, deleteButton, completeButton, incompleteButton));
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.setPadding(new Insets(20));

        return new Scene(root, 600, 400);
    }

    private VBox createInputBox() {
        Label label = new Label("New Task:");
        label.getStyleClass().add("label");

        taskInput.setPromptText("Enter task description");
        taskInput.getStyleClass().add("text-field");

        VBox inputBox = new VBox(label, taskInput);
        inputBox.setSpacing(10);
        return inputBox;
    }

    private HBox createButtonBar(Button... buttons) {
        HBox buttonBar = new HBox(buttons);
        buttonBar.setAlignment(Pos.CENTER);
        buttonBar.getStyleClass().add("button-bar");
        return buttonBar;
    }

    private void handleAddTask(String description) {
        if (!description.isEmpty()) {
            Task newTask = new Task(description);
            tasks.add(newTask);
            saveData();
            taskInput.clear();

            // Provide user feedback
            showAlert(Alert.AlertType.INFORMATION, "Task Added", "Task has been added successfully.");
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please enter a non-empty task description.");
        }
    }

    private void handleDeleteTask(Task selectedTask) {
        if (selectedTask != null) {
            // Confirm deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this task?");
            alert.setContentText(selectedTask.getDescription());

            if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                tasks.remove(selectedTask);
                saveData();

                // Provide user feedback
                showAlert(Alert.AlertType.INFORMATION, "Task Deleted", "Task has been deleted successfully.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Task Selected", "Please select a task to delete.");
        }
    }

    private void handleMarkComplete(Task selectedTask) {
        if (selectedTask != null) {
            selectedTask.markComplete();
            saveData();
            tableView.refresh();
        }
    }

    private void handleMarkIncomplete(Task selectedTask) {
        if (selectedTask != null) {
            selectedTask.markIncomplete();
            saveData();
            tableView.refresh();
        }
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
                String[] parts = line.split("\\|");
                if (parts.length >= 2) {
                    String description = parts[0];
                    boolean completed = Boolean.parseBoolean(parts[1]);

                    Task task = new Task(description);
                    task.setCompleted(completed);
                    tasks.add(task);
                }
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
                String status = task.isCompleted() ? "complete" : "incomplete";
                writer.write(task.getDescription() + "|" + status);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    static class Task {
        private final SimpleStringProperty description;
        private final SimpleBooleanProperty completed;

        public Task(String description) {
            this.description = new SimpleStringProperty(description);
            this.completed = new SimpleBooleanProperty(false);
        }

        public String getDescription() {
            return description.get();
        }

        public void setDescription(String description) {
            this.description.set(description);
        }

        public boolean isCompleted() {
            return completed.get();
        }

        public void setCompleted(boolean completed) {
            this.completed.set(completed);
        }

        public void markComplete() {
            setCompleted(true);
        }

        public void markIncomplete() {
            setCompleted(false);
        }

        public SimpleStringProperty descriptionProperty() {
            return description;
        }

        public SimpleBooleanProperty completedProperty() {
            return completed;
        }

        public SimpleStringProperty statusProperty() {
            return new SimpleStringProperty(getStatus());
        }

        private String getStatus() {
            return isCompleted() ? "complete" : "incomplete";
        }
    }
}
