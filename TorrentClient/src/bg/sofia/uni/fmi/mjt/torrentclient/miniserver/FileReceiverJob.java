package bg.sofia.uni.fmi.mjt.torrentclient.miniserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

public class FileReceiverJob implements Callable<Boolean> {

    @Override
    public Boolean call() throws Exception {

        String serverAddress = "localhost"; // Change this to your server address
        int serverPort = 12345; // Change this to your server port number
        String filePath = "received_file.txt"; // Change this to your desired file path for saving received file

        try (Socket socket = new Socket(serverAddress, serverPort);
             FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            MessageDigest md = MessageDigest.getInstance("MD5");

            while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                md.update(buffer, 0, bytesRead);
            }

            byte[] receivedChecksum = new byte[16]; // MD5 checksum is 16 bytes
            int checksumBytesRead = socket.getInputStream().read(receivedChecksum);

            if (checksumBytesRead == -1) {
                System.out.println("Checksum not received. File transmission interrupted.");
                // Optionally, you can delete the incomplete file
                Files.delete(Path.of(filePath));
            } else {
                byte[] calculatedChecksum = md.digest();

                if (MessageDigest.isEqual(receivedChecksum, calculatedChecksum)) {
                    System.out.println("File received successfully and checksum matches.");
                } else {
                    System.out.println("Checksum mismatch. File may be corrupted.");
                    // Optionally, you can delete the corrupted file
                    Files.delete(Path.of(filePath));
                }
            }
        } catch (IOException e) {
            System.err.println("Error receiving file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return null;
    }
}
