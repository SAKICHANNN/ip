package hhvrfn;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logger for recording application events and errors.
 * Logs are written to a file with timestamps for debugging purposes.
 */
public class Logger {
    private static final Path LOG_FILE = Paths.get("./logs/hhvrfn.log");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static boolean isEnabled = true;

    /** Private constructor to prevent instantiation */
    private Logger() { }

    /**
     * Logs an informational message.
     *
     * @param message The message to log
     */
    public static void info(String message) {
        log("INFO", message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message to log
     */
    public static void warn(String message) {
        log("WARN", message);
    }

    /**
     * Logs an error message.
     *
     * @param message The message to log
     */
    public static void error(String message) {
        log("ERROR", message);
    }

    /**
     * Logs an error with exception details.
     *
     * @param message The message to log
     * @param exception The exception that occurred
     */
    public static void error(String message, Exception exception) {
        log("ERROR", message + " - Exception: " + exception.getClass().getSimpleName()
                + ": " + exception.getMessage());
    }

    /**
     * Disables logging (useful for tests or when storage is unavailable).
     */
    public static void disable() {
        isEnabled = false;
    }

    /**
     * Enables logging.
     */
    public static void enable() {
        isEnabled = true;
    }

    /**
     * Internal method to write log entries.
     *
     * @param level The log level (INFO, WARN, ERROR)
     * @param message The message to log
     */
    private static void log(String level, String message) {
        if (!isEnabled) {
            return;
        }

        try {
            ensureLogDirectoryExists();
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String logEntry = String.format("[%s] %s: %s%n", timestamp, level, message);

            try (BufferedWriter writer = Files.newBufferedWriter(LOG_FILE, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(logEntry);
            }
        } catch (IOException e) {
            // If logging fails, disable it to prevent cascading errors
            // Print to stderr as a fallback
            System.err.println("Warning: Failed to write to log file. Logging disabled. Error: " + e.getMessage());
            isEnabled = false;
        }
    }

    /**
     * Ensures the log directory exists.
     *
     * @throws IOException if the directory cannot be created
     */
    private static void ensureLogDirectoryExists() throws IOException {
        Path logDir = LOG_FILE.getParent();
        if (logDir != null && !Files.exists(logDir)) {
            Files.createDirectories(logDir);
        }
    }
}
