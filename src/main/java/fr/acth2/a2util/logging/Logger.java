package fr.acth2.a2util.logging;

import fr.acth2.a2util.A2Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static boolean hasInitialized = false;
    private static String LOG_DIR;
    private static String LOG_FILE;
    private static String lastLogContent = null;
    private static int repeatCount = 0;

    public static void initialize(String name) {
        LOG_DIR = System.getProperty("user.home") + "/." + name;
        LOG_FILE = LOG_DIR + "/output.log";

        new File(LOG_DIR).mkdirs();
        hasInitialized = true;
    }

    private static void checkLogFileSize() {
        File logFile = new File(LOG_FILE);
        if (logFile.length() > 1024 * 2048) {
            String backupFile = LOG_DIR + "/a2bt_" + System.currentTimeMillis() + ".log";
            logFile.renameTo(new File(backupFile));
        }
    }

    public static void log(String log) {
        if (hasInitialized) {
            handleLog(log, false);
        } else {
            System.err.println(formatLog("The Logger is not initialized."));
        }
    }

    public static void debugLog(String log) {
        if(A2Utils.getProgramArguments().isDebug()) {
            if (hasInitialized) {
                handleLog("[DEBUG] " + log, false);
            } else {
                System.err.println(formatLog("The Logger is not initialized."));
            }
        }
    }

    public static void err(String err) {
        if (hasInitialized) {
            handleLog(err, true);
        } else {
            System.err.println(formatLog("The Logger is not initialized."));
        }
    }

    private static void handleLog(String message, boolean isError) {
        String formattedLog = formatLog(message);
        String logContent = message.trim();

        if (logContent.equals(lastLogContent)) {
            repeatCount++;
        } else {
            if (repeatCount > 0) {
                String repeatedLog = formatLog(lastLogContent) + " [REPEATED " + repeatCount + " TIMES]";
                writeToFile(repeatedLog);
            }

            writeToFile(formattedLog);
            lastLogContent = logContent;
            repeatCount = 0;
        }

        if (isError) {
            System.err.println(formattedLog);
        } else {
            System.out.println(formattedLog);
        }
    }

    private static String formatLog(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return "[" + dtf.format(now) + "] " + message;
    }

    private static void writeToFile(String log) {
        String cleanedLog = log.replaceAll("\u001B\\[[;\\d]*m", "");
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(cleanedLog + System.lineSeparator());
            checkLogFileSize();
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

}