import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final Path filePath;

    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    public List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File file = filePath.toFile();

        if (!file.exists()) {
            System.out.println("Data file not found. Starting with an empty task list.");
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            int lineNumber = 0;
            while (scanner.hasNextLine()) {
                lineNumber++;
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    Task task = parseTask(line);
                    tasks.add(task);
                } catch (IllegalArgumentException e) {
                    System.out.println("Warning: Corrupted data at line " + lineNumber 
                        + " (" + e.getMessage() + "). Skipping line.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return tasks;
    }

    public void save(List<Task> tasks) {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }

            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                for (Task task : tasks) {
                    writer.write(task.toFileFormat() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks to disk: " + e.getMessage());
        }
    }

    private Task parseTask(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Insufficient fields");
        }

        String type = parts[0];
        if (!type.equals("T") && !type.equals("D") && !type.equals("E")) {
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (!parts[1].equals("0") && !parts[1].equals("1")) {
            throw new IllegalArgumentException("Invalid completion status: " + parts[1]);
        }

        int expectedFields = type.equals("T") ? 3 : type.equals("D") ? 4 : 5;
        if (parts.length != expectedFields) {
            throw new IllegalArgumentException("Invalid number of fields for task type " + type);
        }

        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        if (description.isBlank()) {
            throw new IllegalArgumentException("Task description cannot be empty");
        }

        Task task;
        switch (type) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts[3].isBlank()) {
                throw new IllegalArgumentException("Deadline date cannot be empty");
            }
            task = new Deadline(description, parts[3]);
            break;
        case "E":
            if (parts[3].isBlank() || parts[4].isBlank()) {
                throw new IllegalArgumentException("Event times cannot be empty");
            }
            task = new Event(description, parts[3], parts[4]);
            break;
        default:
            throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }
}
