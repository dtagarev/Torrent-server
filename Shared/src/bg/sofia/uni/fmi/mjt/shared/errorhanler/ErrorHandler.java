package bg.sofia.uni.fmi.mjt.shared.errorhanler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class ErrorHandler {
    private final Path logPath;

    public ErrorHandler(Path logPath) {
        
        this.logPath = logPath;

        if (!Files.exists(logPath)) {
            try {
                Files.createFile(logPath);
            } catch (IOException e) {
                System.out.println("An error occurred while creating the file.");
            }
        }

    }

    public synchronized void writeToLogFile(Exception e) {
        try (FileWriter fw = new FileWriter(logPath.toString(), true); PrintWriter pw = new PrintWriter(fw)) {

            LocalDateTime time = LocalDateTime.now();
            String timeStamp = time.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            pw.println(timeStamp);

            pw.println(e.getMessage());

            e.printStackTrace(pw);
            pw.println("--------------------------------------------------");
        } catch (IOException ex) {
            System.err.println("Error writing to log file");
        }

    }

    public synchronized void writeToLogFile(Exception e, String message) {
        try (FileWriter fw = new FileWriter(logPath.toString(), true); PrintWriter pw = new PrintWriter(fw)) {

            LocalDateTime time = LocalDateTime.now();
            String timeStamp = time.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            pw.println(timeStamp);

            pw.println(e.getMessage());

            e.printStackTrace(pw);

            pw.println();
            pw.println("Message: " + message);

            pw.println("--------------------------------------------------");
        } catch (IOException ex) {
            System.err.println("Error writing to log file");
        }

    }

}
