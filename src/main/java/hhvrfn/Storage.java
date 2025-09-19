package hhvrfn;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Handles persisting tasks to disk and loading them at startup.
 * Uses a simple line-based format, e.g.:
 * T | 1 | read book
 * D | 0 | return book | June 6th
 * E | 0 | project meeting | Aug 6th 2-4pm
 */
public class Storage {
    private final Path dataFile;

    /**
     * Constructs a storage pointing to the given relative file path.
     *
     * @param relativePath The relative path from project root (e.g., "./data/hhvrfn.txt").
     */
    public Storage(String relativePath) {
        this.dataFile = Paths.get(relativePath);
    }

    /**
     * Loads tasks from disk. If the folder/file does not exist, returns an empty list.
     * Corrupted lines are skipped (stretch: tolerate and continue).
     *
     * @return The list of tasks loaded.
     * @throws HhvrfnException If an unrecoverable I/O error occurs.
     */
    public ArrayList<Task> load() throws HhvrfnException {
        Logger.info("Loading tasks from: " + dataFile);
        ArrayList<Task> result = new ArrayList<>();
        try {
            ensureFileExists();
            for (String line : Files.readAllLines(dataFile, StandardCharsets.UTF_8)) {
                Task t = parseLine(line);
                if (t != null) {
                    result.add(t);
                }
            }
            Logger.info("Successfully loaded " + result.size() + " tasks");
            return result;
        } catch (AccessDeniedException e) {
            Logger.error("Failed to load data - permission denied", e);
            throw new HhvrfnException("Cannot load data: Permission denied. "
                    + "Please check if you have read access to the file: " + dataFile);
        } catch (NoSuchFileException e) {
            Logger.warn("Data file not found, will create new file: " + dataFile);
            throw new HhvrfnException("Cannot load data: Data file not found at expected location: "
                    + dataFile + ". A new file will be created when you add tasks.");
        } catch (FileSystemException e) {
            Logger.error("File system error during load", e);
            throw new HhvrfnException("Cannot load data: File system error. "
                    + "The storage location might be on a read-only drive or network location is unavailable: "
                    + e.getMessage());
        } catch (IOException e) {
            Logger.error("I/O error during load", e);
            throw new HhvrfnException("Cannot load data: An unexpected error occurred while reading the file. "
                    + "The file might be corrupted or in use by another program: " + e.getMessage());
        }
    }

    /**
     * Saves all tasks to disk, overwriting the file.
     *
     * @param tasks The tasks to persist.
     * @throws HhvrfnException If an I/O error occurs.
     */
    public void save(ArrayList<Task> tasks) throws HhvrfnException {
        Logger.info("Saving " + tasks.size() + " tasks to: " + dataFile);
        try {
            ensureFileExists();
            try (BufferedWriter bw = Files.newBufferedWriter(dataFile, StandardCharsets.UTF_8)) {
                for (Task t : tasks) {
                    bw.write(serialize(t));
                    bw.newLine();
                }
            }
            Logger.info("Successfully saved tasks to file");
        } catch (AccessDeniedException e) {
            Logger.error("Failed to save data - permission denied", e);
            throw new HhvrfnException("Cannot save data: Permission denied. "
                    + "Please check if you have write access to the file location: " + dataFile);
        } catch (FileSystemException e) {
            String message = e.getMessage();
            if (message != null && message.toLowerCase().contains("space")) {
                Logger.error("Failed to save data - insufficient disk space", e);
                throw new HhvrfnException("Cannot save data: Not enough disk space available. "
                        + "Please free up some space and try again.");
            } else {
                Logger.error("File system error during save", e);
                throw new HhvrfnException("Cannot save data: File system error. "
                        + "The storage location might be on a read-only drive or network location is unavailable: "
                        + message);
            }
        } catch (IOException e) {
            Logger.error("I/O error during save", e);
            throw new HhvrfnException("Cannot save data: An unexpected error occurred while writing to the file. "
                    + "The file might be in use by another program or the storage device might have issues: "
                    + e.getMessage());
        }
    }

    private void ensureFileExists() throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null && !Files.exists(parent)) {
            try {
                Files.createDirectories(parent);
            } catch (AccessDeniedException e) {
                throw new IOException("Cannot create directory: Permission denied for " + parent, e);
            } catch (FileSystemException e) {
                throw new IOException("Cannot create directory: File system error for " + parent + ": "
                        + e.getMessage(), e);
            }
        }
        if (!Files.exists(dataFile)) {
            try {
                Files.createFile(dataFile);
            } catch (AccessDeniedException e) {
                throw new IOException("Cannot create file: Permission denied for " + dataFile, e);
            } catch (FileSystemException e) {
                throw new IOException("Cannot create file: File system error for " + dataFile + ": "
                        + e.getMessage(), e);
            }
        }
    }

    // --- Format helpers: "T|1|desc", "D|0|desc|by", "E|0|desc|from-to"

    private String serialize(Task t) {
        assert t != null : "Storage.serialize(): task must be non-null";
        String done = t.getStatusIcon().equals("X") ? "1" : "0";
        if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            assert d.by != null : "Storage.serialize(): deadline 'by' is null";
            // persist ISO format: yyyy-MM-dd
            return "D | " + done + " | " + d.description + " | "
                    + d.by.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } else if (t instanceof Event) {
            Event e = (Event) t;
            assert e.from != null && e.to != null : "Storage.serialize(): event 'from/to' is null";
            return "E | " + done + " | " + e.description + " | " + e.from + " to " + e.to;
        } else { // Todo or legacy Task treated as TODO
            return "T | " + done + " | " + t.description;
        }
    }

    private Task parseLine(String line) {
        if (line == null) {
            return null;
        }
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        // Split only on " | " to keep user text intact.
        String[] parts = trimmed.split("\\s\\|\\s");
        try {
            if (parts.length < 3) {
                return null; // corrupted
            }
            String type = parts[0];
            boolean done = "1".equals(parts[1]);
            String desc = parts[2];

            Task t;
            switch (type) {
            case "T":
                t = new Todo(desc);
                break;
            case "D":
                if (parts.length < 4) {
                    return null;
                }
                LocalDate by = LocalDate.parse(parts[3]); // expect ISO yyyy-MM-dd
                t = new Deadline(desc, by);
                break;
            case "E":
                if (parts.length < 4) {
                    return null;
                }
                // We stored "from to to" as one field; try to split by " to " once
                String field = parts[3];
                int sep = field.indexOf(" to ");
                String from = sep >= 0 ? field.substring(0, sep) : field;
                String to = sep >= 0 ? field.substring(sep + 4) : "";
                t = new Event(desc, from, to);
                break;
            default:
                return null; // unknown type
            }

            if (done) {
                t.markAsDone();
            }
            return t;
        } catch (Exception ex) {
            // Corrupted: skip line
            return null;
        }
    }
}
