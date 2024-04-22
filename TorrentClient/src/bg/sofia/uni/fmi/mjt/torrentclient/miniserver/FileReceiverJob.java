package bg.sofia.uni.fmi.mjt.torrentclient.miniserver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.concurrent.Callable;

public class FileReceiverJob implements Callable<Boolean> {
    private Socket serverSocket;
    private Path receivedFileLocation;

    public FileReceiverJob(Socket serverSocket, Path receivedFileLocation) {
        this.serverSocket = serverSocket;
        this.receivedFileLocation = receivedFileLocation;
    }

    //@Override
    //public Boolean call() throws Exception {

    //    String serverAddress = "localhost"; // Change this to your server address
    //    int serverPort = 12345; // Change this to your server port number
    //    String filePath = "received_file.txt"; // Change this to your desired file path for saving received file

    //    try (Socket socket = new Socket(serverAddress, serverPort);
    //         FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {

    //        byte[] buffer = new byte[1024];
    //        int bytesRead;
    //        MessageDigest md = MessageDigest.getInstance("MD5");

    //        while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {
    //            fileOutputStream.write(buffer, 0, bytesRead);
    //            md.update(buffer, 0, bytesRead);
    //        }

    //        byte[] receivedChecksum = new byte[16]; // MD5 checksum is 16 bytes
    //        int checksumBytesRead = socket.getInputStream().read(receivedChecksum);

    //        if (checksumBytesRead == -1) {
    //            System.out.println("Checksum not received. File transmission interrupted.");
    //            // Optionally, you can delete the incomplete file
    //            Files.delete(Path.of(filePath));
    //        } else {
    //            byte[] calculatedChecksum = md.digest();

    //            if (MessageDigest.isEqual(receivedChecksum, calculatedChecksum)) {
    //                System.out.println("File received successfully and checksum matches.");
    //            } else {
    //                System.out.println("Checksum mismatch. File may be corrupted.");
    //                // Optionally, you can delete the corrupted file
    //                Files.delete(Path.of(filePath));
    //            }
    //        }
    //    } catch (IOException e) {
    //        System.err.println("Error receiving file: " + e.getMessage());
    //    } catch (Exception e) {
    //        System.err.println("Error: " + e.getMessage());
    //    }
    //    return null;
    //}
    @Override
    public Boolean call() {
        //connect to server, ask for file, receive file, save file

        try(FileChannel fileChannel = FileChannel.open(receivedFileLocation)) {

            //How to use corecctly
            fileChannel.transferFrom(serverSocket.getChannel(), 0, Long.MAX_VALUE);

        } catch (AsynchronousCloseException e) {
            // try with this exception, the other option is ClosedByInterruptException

            //error handler . write to log file
            //delete the file junk.
            //return false;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}